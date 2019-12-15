package listener.main;

import java.util.Hashtable;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import generated.MiniCBaseListener;
import generated.MiniCParser;
import generated.MiniCParser.ExprContext;
import generated.MiniCParser.Fun_declContext;
import generated.MiniCParser.Local_declContext;
import generated.MiniCParser.ParamsContext;
import generated.MiniCParser.ProgramContext;
import generated.MiniCParser.StmtContext;
import generated.MiniCParser.Type_specContext;
import generated.MiniCParser.Var_declContext;

import static listener.main.x86GenListenerHelper.*;
import static listener.main.SymbolTable.*;

public class x86GenListener extends MiniCBaseListener implements ParseTreeListener {
	ParseTreeProperty<String> newTexts = new ParseTreeProperty<String>();
	SymbolTable symbolTable = new SymbolTable();

	// program : decl+

	@Override
	public void enterFun_decl(MiniCParser.Fun_declContext ctx) {
		symbolTable.initFunDecl();
		ParamsContext params = (MiniCParser.ParamsContext) ctx.getChild(3);
		symbolTable.putParams(params);
		// Not Implemented
	}

	// var_decl : type_spec IDENT ';' | type_spec IDENT '=' LITERAL ';'|type_spec
	// IDENT '[' LITERAL ']' ';'
	@Override
	public void enterVar_decl(MiniCParser.Var_declContext ctx) {
		String varname = getGlobalVarName(ctx);
		if (isArrayDecl(ctx)) {
			symbolTable.putglobalArray(varname, Type.INT);
		} else if (isDeclWithInit(ctx)) {
			symbolTable.putGlobalVarWithInitVal(varname, Type.INT, initVal(ctx));
		} else {
			symbolTable.putGlobalVarWithInitVal(varname, Type.INT, 0);
		}
	}

	@Override
	public void enterLocal_decl(MiniCParser.Local_declContext ctx) {
		String varname = getLocalVarName(ctx);
		if (isArrayDecl(ctx)) {
			symbolTable.putLocalArray(varname, Type.INT, get_localArraysize(ctx));
		} else if (isDeclWithInit(ctx)) {
			symbolTable.putLocalVarWithInitVal(varname, Type.INT, initVal(ctx));
		} else {
			symbolTable.putLocalVar(varname, Type.INT);
		}
	}

	@Override
	public void exitProgram(MiniCParser.ProgramContext ctx) {
		String func = "";
		String var = "section .data\n" + "format db \"%d\", 10, 0\n";
		String var_array = "";
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.decl(i).fun_decl() != null)
				if (newTexts.get(ctx.decl(i)) != null)
					func += newTexts.get(ctx.decl(i));
		}
		List a = ctx.decl();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.decl(i).var_decl() != null)
				if (newTexts.get(ctx.decl(i)) != null && ctx.decl(i).getChild(0).getChildCount() != 6)
					var += newTexts.get(ctx.decl(i));
				else if (newTexts.get(ctx.decl(i)) != null && ctx.decl(i).getChild(0).getChildCount() == 6) {
					var_array = newTexts.get(ctx.decl(i));
				}
		}

		func = "section .text\n" + "global main\n" + "extern printf\n\n" + func + "\n\n";

		
		for (String key : symbolTable.getStringTable().keySet()){
			var += symbolTable.getString(key);
		}
		var_array = "section .bss\n" + var_array;
		String str = func + var + var_array;
		newTexts.put(ctx, str);
		System.out.println(newTexts.get(ctx));
	}

	// decl : var_decl | fun_decl
	@Override
	public void exitDecl(MiniCParser.DeclContext ctx) {
		newTexts.put(ctx, newTexts.get(ctx.getChild(0)));
	}

	// stmt : expr_stmt | compound_stmt | if_stmt | while_stmt | return_stmt
	@Override
	public void exitStmt(MiniCParser.StmtContext ctx) {
		newTexts.put(ctx, newTexts.get(ctx.getChild(0)));
	}

	// expr_stmt : expr ';'
	@Override
	public void exitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
		newTexts.put(ctx, newTexts.get(ctx.getChild(0)));
	}

	// while_stmt : WHILE '(' expr ')' stmt
	@Override
	public void exitWhile_stmt(MiniCParser.While_stmtContext ctx) {
		String stmt = "";
		String expr = newTexts.get(ctx.expr());
		String bodystmt = newTexts.get(ctx.stmt());

		String lend = symbolTable.newLabel();
		String lelse = symbolTable.newLabel();

		stmt += "jmp " + lelse + "\n" + lend + ": \n" + bodystmt + lelse + ": " + "\n" + expr + "cmp eax, 0\n" + "jne "
				+ lend + "\n";

		newTexts.put(ctx, stmt);
	}

	@Override
	public void exitFun_decl(MiniCParser.Fun_declContext ctx) {
		// Not Implemented
		String str = "";
		str += getFunName(ctx) + ":\n";
		str += "push ebp\n";
		str += "mov ebp, esp\n";
		str += "push ebx\n";
		str += "sub esp, " + symbolTable.getTotalLocalOffset() + "\n";
		str += newTexts.get(ctx.getChild(ctx.getChildCount() - 1));
		if( isVoidF(ctx)) {
			str += "mov ebx, dword[ebp - 4]\n";
			str += "leave\n";
			str += "ret\n\n";
		}
		newTexts.put(ctx, str);
	}

	@Override
	public void exitVar_decl(MiniCParser.Var_declContext ctx) {
		String varname = getGlobalVarName(ctx);
		String str = "";
		if (isArrayDecl(ctx)) {
			str += varname + " resd " + initsize(ctx) + "\n";
		} else if (isDeclWithInit(ctx)) {
			str += varname + " DD " + initVal(ctx) + "\n";
		} else {
			str += varname + " DD 0\n";
		}

		newTexts.put(ctx, str);
	}

	// compound_stmt : '{' local_decl* stmt* '}'
	@Override
	public void exitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
		String str = "";

		for (int i = 0; i < ctx.local_decl().size(); i++)
			str += newTexts.get(ctx.local_decl(i));
		for (int i = 0; i < ctx.stmt().size(); i++)
			str += newTexts.get(ctx.stmt(i));

		newTexts.put(ctx, str);
	}

	@Override
	public void exitLocal_decl(MiniCParser.Local_declContext ctx) {
		String varname = getLocalVarName(ctx);
		String str = "";
		if (isArrayDecl(ctx)) {
			// not essential
		} else if (isDeclWithInit(ctx)) {
			str += "mov dword [ebp - " + symbolTable.getLocalOffset(varname) + "], " + initVal(ctx) + "\n";
		} else {

		}

		newTexts.put(ctx, str);
	}

	// if_stmt : IF '(' expr ')' stmt | IF '(' expr ')' stmt ELSE stmt;
	@Override
	public void exitIf_stmt(MiniCParser.If_stmtContext ctx) {
		String stmt = "";
		String condExpr = newTexts.get(ctx.expr());
		String thenStmt = newTexts.get(ctx.stmt(0));

		String lend = symbolTable.newLabel();
		String lelse = symbolTable.newLabel();

		if (noElse(ctx)) {
			stmt += condExpr + "cmp eax, 0\n" + "je " + lend + "\n" + thenStmt + lend + ":" + "\n";
		} else {
			String elseStmt = newTexts.get(ctx.stmt(1));
			stmt += condExpr + "cmp eax, 0\n" + "je " + lelse + "\n" + thenStmt + "jmp " + lend + "\n" + lelse + ": \n"
					+ elseStmt + lend + ":" + "\n";
		}

		newTexts.put(ctx, stmt);
	}

	// return_stmt : RETURN ';' | RETURN expr ';'
	@Override
	public void exitReturn_stmt(MiniCParser.Return_stmtContext ctx) {
		String ret = newTexts.get(ctx.expr());
		ret += "mov ebx, dword[ebp - 4]\n";
		ret += "leave\n";
		ret += "ret\n\n";
		newTexts.put(ctx, ret );
		// Not Implemented
	}

	@Override
	public void exitExpr(MiniCParser.ExprContext ctx) {
		String expr = "";

		if (ctx.getChildCount() <= 0) {
			newTexts.put(ctx, "");
			return;
		}
		if (ctx.getChildCount() == 1) { // IDENT | LITERAL
			if (symbolTable.isglobalArray(ctx)) //global array
			{
				newTexts.put(ctx, "");
				return;
			}else if (ctx.STRING() != null){
				String target = ctx.getText();
				if(target.contains("\\")) {
					target = getString_withEscape(target);
				}
				else {
					target+=", 0";
				}
				symbolTable.putString(target);
				//String key = getStringFormat(symbolTable.getString(target));
				String key = getStringFormat(symbolTable.getString(target));
				expr += "mov eax, "+key+"\n";

			}
			else if (ctx.IDENT() != null) {
				String varname = ctx.getChild(0).getText();
				if (symbolTable.isLocalVar(varname))//local variable
					expr += "mov eax, dword [ebp - " + symbolTable.getLocalOffset(varname) + "]\n";
				else if( symbolTable.isargVar(varname))
					expr += "mov eax, [ebp + " + symbolTable.getargOffset(varname) + "]\n";
				else if (symbolTable.isglobalVar(varname))
					expr += "mov eax, [ " + varname + " ]\n";
				else
					expr += "Not found var\n";
			} else if (ctx.LITERAL() != null) {
				if (symbolTable.isglobalVar(ctx.getParent().getChild(0).getText())
						&&ctx.getParent().getChild(1).getText().equals("=")) {// global a =3;
					newTexts.put(ctx, ctx.getChild(0).getText());
					return;

				}
				else {
					expr += "mov eax, " + ctx.LITERAL().getText() + "\n";
				}
			}

		} else if (ctx.getChildCount() == 2) { // UnaryOperation
			expr = handleUnaryExpr(ctx, expr);
		} else if (ctx.getChildCount() == 3) {
			
			if (symbolTable.isLocalVar(ctx.getChild(0).getText()) 
					&& ctx.getChild(1).getText().contentEquals("=")) {
				
				String varname = ctx.getChild(0).getText();
				
				if(ctx.getChild(2).getChildCount()>1 ||
						(ctx.getChild(2).getChildCount()==1 &&symbolTable.is_existVar(ctx.getChild(2).getText()))) {

					//로컬 변수에 변수 혹은 계산식이 들어갈때
					expr+= newTexts.get(ctx.getChild(2))+
							"mov dword [ebp - " + symbolTable.getLocalOffset(varname) + "], eax\n";
				}
				else {//로컬 변수에 상수가 들어갈때
					expr += "mov dword [ebp - " + symbolTable.getLocalOffset(varname) + "], " + newTexts.get(ctx.getChild(2))
							+ "\n";
				}
			} 
			else if (symbolTable.isglobalVar(ctx.getChild(0).getText())
					&& ctx.getChild(1).getText().equals("=")) {
				
				String varname = ctx.getChild(0).getText();
				
				if(ctx.getChild(2).getChildCount()>1 ||
						(ctx.getChild(2).getChildCount()==1 &&symbolTable.is_existVar(ctx.getChild(2).getText()))) {
					//글로벌 변수에 변수 혹은 계산식이 들어갈때
					expr+= newTexts.get(ctx.getChild(2))+"\n"+"mov dword [" +varname + "], eax\n";
				}
				else{
					//글로벌 변수에 상수가 들어갈때
					expr += "mov dword [" +varname + "], " + newTexts.get(ctx.getChild(2))+ "\n";
				}


			} else if (ctx.getChild(0).getText().equals("(")) { // '(' expr ')'

			} else if (ctx.getChild(1).getText().equals("=")) { // IDENT '=' expr
				String varname = ctx.getChild(0).getText();
				expr += newTexts.get(ctx.getChild(2));
				expr += "mov dword [ebp - " + symbolTable.getLocalOffset(varname) + "], eax\n";
			} else { // binary operation
				expr = handleBinExpr(ctx, expr);
			}
		}
		// IDENT '(' args ')' | IDENT '[' expr ']'
		else if (ctx.getChildCount() == 4) {
			
			if (ctx.args() != null) { // function calls
				expr = handleFunCall(ctx, expr);
			} 
			else if (symbolTable.isglobalVar(ctx.getChild(0).getText())) { // find global offset to print
				String varname = ctx.getChild(0).getText();
				int index = get_globalarrayindex(ctx) * 4;
				if (index == 0) {
					expr += "mov eax , dword [" + varname + "] \n";
				} else {
					expr += "mov eax , dword [" + varname + "+" + index + "] \n";
				}
			} 
			else {//Find local offset to print
				String varname = ctx.getChild(0).getText();
				int offset = symbolTable.getLocalOffset(varname) - get_intarrayindex(ctx);
				expr += "mov eax, dword [ebp - " + offset + "]\n";
			}
		}

		// IDENT '[' expr ']' '=' expr
		else { // Arrays: TODO
			if (!symbolTable.isglobalVar(ctx.getParent().getChild(0).getText())) {
				String varname = ctx.getChild(0).getText();
				
				if (symbolTable.isglobalVar(varname)) {
					
					int index = get_globalarrayindex(ctx) * 4;
					
					if(ctx.getChild(5).getChildCount()>1
							|| (ctx.getChild(5).getChildCount()==1 &&symbolTable.is_existVar(ctx.getChild(5).getText()))) {

						//글로벌 배열에 변수 혹은 계산식이 들어갈때

						expr+= newTexts.get(ctx.getChild(5));
						
						if (index == 0) {
							expr += "mov dword [" + varname + "] , eax \n";
						} else {
							expr += "mov dword [" + varname + "+" + index + "] , eax \n";
						}
					}

					else{//글로벌 배열에 상수가 들어갈때

						int operand = get_operand_intvalue(ctx);
						
						if (index == 0) {
							expr += "mov dword [" + varname + "] , "+ operand+" \n";
						} else {
							expr += "mov dword [" + varname + "+" + index + "] , "+ operand+"\n";
						}
					}
				} else {//local
					
					int offset = symbolTable.getLocalOffset(varname) - get_intarrayindex(ctx);
					
					if(ctx.getChild(5).getChildCount()>1  || 
							(ctx.getChild(5).getChildCount()==1 &&symbolTable.is_existVar(ctx.getChild(5).getText()))) {

						//로컬 배열에 변수 혹은 계산식의 값이 들어갈때
						expr+= newTexts.get(ctx.getChild(5))+"mov dword [ebp - " + offset + "], eax \n";
					}
					else {//로컬 배열에 상수값이 들어갈때
						expr += "mov dword [ebp - " + offset + "], " + get_operand_intvalue(ctx) + "\n";
					}
				}

			}

		}
		newTexts.put(ctx, expr);
	}

	private String handleUnaryExpr(MiniCParser.ExprContext ctx, String expr) {
		String l1 = symbolTable.newLabel();
		String l2 = symbolTable.newLabel();
		String lend = symbolTable.newLabel();

		expr += newTexts.get(ctx.expr(0));
		switch (ctx.getChild(0).getText()) {
			case "-":
				expr += "neg eax\n";
				break;
			case "--":
				expr += "dec eax\n";
				break;
			case "++":
				expr += "inc eax\n";
				break;
			case "!":
				expr += "cmp eax, 0\n" + "je " + l2 + "\n" + l1 + ":\n" + "mov eax, 0\n" + "jmp " + lend + "\n" + l2 + ":\n"
						+ "mov eax, 1\n" + lend + ":\n";
				break;
		}
		return expr;
	}

	private String handleBinExpr(MiniCParser.ExprContext ctx, String expr) {
		String l2 = symbolTable.newLabel();
		String lend = symbolTable.newLabel();

		String expr1 = newTexts.get(ctx.expr(0));
		String expr2 = newTexts.get(ctx.expr(1));

		switch (ctx.getChild(1).getText()) {
			case "*":
				expr += expr1;
				expr += "mov ebx, eax\n";
				expr += expr2;
				expr += "imul eax, ebx\n";
				break;
			case "/":
				expr += expr1;
				expr += "mov ebx, eax\n";
				expr += expr2;
				expr += "mov ecx, eax\n";
				expr += "mov eax, ebx\n";
				expr += "mov edx, 0\n";
				expr += "div ecx\n";
				break;
			case "%":
				expr += expr1;
				expr += "mov ebx, eax\n";
				expr += expr2;
				expr += "mov ecx, eax\n";
				expr += "mov eax, ebx\n";
				expr += "mov edx, 0\n";
				expr += "div ecx\n";
				expr += "mov eax, edx\n";
				break;
			case "+": // expr(0) expr(1) iadd
				expr += expr1;
				expr += "mov ebx, eax\n";
				expr += expr2;
				expr += "add eax, ebx\n";
				break;
			case "-":
				expr += expr1;
				expr += "mov ecx, eax\n";
				if (isNumeric(ctx.expr(1).getText())) {
					expr += "sub eax, "+ctx.expr(1).getText()+"\n";
				}
				else{
					expr += expr2;
					expr += "mov ebx, eax\n";
					expr += "mov eax, ecx\n";
					expr += "sub eax, ebx\n";
				}
				break;
			case "==":
				expr += expr1;
				expr += "mov ecx, eax\n";
				expr += expr2;
				expr += "mov ebx, eax\n";
				expr += "mov eax, ecx\n";
				expr += "cmp eax, ebx\n";
				expr += "je " + l2 + "\n";
				expr += "mov eax, 0\n";
				expr += "jmp " + lend + "\n";
				expr += l2 + ":\n";
				expr += "mov eax, 1\n";
				expr += lend + ":\n";
				break;
			case "!=":
				expr += expr1;
				expr += "mov ecx, eax\n";
				expr += expr2;
				expr += "mov ebx, eax\n";
				expr += "mov eax, ecx\n";
				expr += "cmp eax, ebx\n";
				expr += "jne " + l2 + "\n";
				expr += "mov eax, 0\n";
				expr += "jmp " + lend + "\n";
				expr += l2 + ":\n";
				expr += "mov eax, 1\n";
				expr += lend + ":\n";
				break;
			case "<=":
				expr += expr1;
				expr += "mov ecx, eax\n";
				expr += expr2;
				expr += "mov ebx, eax\n";
				expr += "mov eax, ecx\n";
				expr += "cmp eax, ebx\n";
				expr += "jle " + l2 + "\n";
				expr += "mov eax, 0\n";
				expr += "jmp " + lend + "\n";
				expr += l2 + ":\n";
				expr += "mov eax, 1\n";
				expr += lend + ":\n";
				break;
			case "<":
				expr += expr1;
				expr += "mov ecx, eax\n";
				expr += expr2;
				expr += "mov ebx, eax\n";
				expr += "mov eax, ecx\n";
				expr += "cmp eax, ebx\n";
				expr += "jl " + l2 + "\n";
				expr += "mov eax, 0\n";
				expr += "jmp " + lend + "\n";
				expr += l2 + ":\n";
				expr += "mov eax, 1\n";
				expr += lend + ":\n";
				break;
			case ">=":
				expr += expr1;
				expr += "mov ecx, eax\n";
				expr += expr2;
				expr += "mov ebx, eax\n";
				expr += "mov eax, ecx\n";
				expr += "cmp eax, ebx\n";
				expr += "jge " + l2 + "\n";
				expr += "mov eax, 0\n";
				expr += "jmp " + lend + "\n";
				expr += l2 + ":\n";
				expr += "mov eax, 1\n";
				expr += lend + ":\n";
				break;
			case ">":
				expr += expr1;
				expr += "mov ecx, eax\n";
				expr += expr2;
				expr += "mov ebx, eax\n";
				expr += "mov eax, ecx\n";
				expr += "cmp eax, ebx\n";
				expr += "jg " + l2 + "\n";
				expr += "mov eax, 0\n";
				expr += "jmp " + lend + "\n";
				expr += l2 + ":\n";
				expr += "mov eax, 1\n";
				expr += lend + ":\n";
				break;
			case "and":
				expr += expr1;
				expr += "mov ebx, eax\n";
				expr += expr2;
				expr += "and eax, ebx\n";
				break;
			case "or":
				expr += expr1;
				expr += "mov ebx, eax\n";
				expr += expr2;
				expr += "or eax, ebx\n";
				break;

		}
		return expr;
	}

	private String handleFunCall(MiniCParser.ExprContext ctx, String expr) {
		String funName = getFunName(ctx);
		if (funName.equals("print_d")) {
			expr = newTexts.get(ctx.args()) + "push dword format\n" + "call printf\n";
		}
		else{
			int childCount = ctx.args().getChildCount();
			int args = (childCount+1) - (childCount+1)/2;
			expr += newTexts.get(ctx.args())
					+ "call " + funName + "\n";
			expr += "add esp, "+args*4+"\n";
		}
		return expr;
	}


	// args : expr (',' expr)* | ;
	@Override
	public void exitArgs(MiniCParser.ArgsContext ctx) {
		String argsStr = "";
		for (int i = ctx.expr().size() - 1; i >= 0 ; i--) {
			String target_arg = ctx.expr(i).getText();
			if( isNumeric(ctx.expr(i).getText()) )
				argsStr += "push " + Integer.parseInt(target_arg) +"\n";
			else if( symbolTable.isLocalVar(target_arg) )
				argsStr += "push dword [ebp - " + symbolTable.getLocalOffset(target_arg) + "]\n";
			else {
				argsStr += newTexts.get(ctx.expr(i));
				argsStr += "push dword eax\n";
			}
		}
		newTexts.put(ctx, argsStr);
	}

}
