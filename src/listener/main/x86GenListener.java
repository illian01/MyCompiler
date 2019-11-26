package listener.main;

import java.util.Hashtable;

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
	
	// program	: decl+
	
	@Override
	public void enterFun_decl(MiniCParser.Fun_declContext ctx) {
		symbolTable.initFunDecl();
		// Not Implemented
	}
	
	// var_decl	: type_spec IDENT ';' | type_spec IDENT '=' LITERAL ';'|type_spec IDENT '[' LITERAL ']' ';'
	@Override
	public void enterVar_decl(MiniCParser.Var_declContext ctx) {
		String varname = getGlobalVarName(ctx);
		if(isArrayDecl(ctx)) {
			// Not Implemented
		}
		else if(isDeclWithInit(ctx)) {
			symbolTable.putGlobalVarWithInitVal(varname, Type.INT, initVal(ctx));
		}
		else {
			symbolTable.putGlobalVarWithInitVal(varname, Type.INT, 0);
		}
	}

	
	@Override
	public void enterLocal_decl(MiniCParser.Local_declContext ctx) {			
		String varname = getLocalVarName(ctx);
		if(isArrayDecl(ctx)) {
			// Not Implemented
		}
		else if(isDeclWithInit(ctx)) {
			symbolTable.putLocalVarWithInitVal(varname, Type.INT, initVal(ctx));
		}
		else {
			symbolTable.putLocalVar(varname, Type.INT);
		}
	}

	
	@Override
	public void exitProgram(MiniCParser.ProgramContext ctx) {
		String func = "";
		String var = "";
		
		for(int i = 0; i < ctx.getChildCount(); i++) {
			if(ctx.decl(i).fun_decl() != null)
				if(newTexts.get(ctx.decl(i)) != null)
					func += newTexts.get(ctx.decl(i));
		}
		
		for(int i = 0; i < ctx.getChildCount(); i++) {
			if(ctx.decl(i).var_decl() != null)
				if(newTexts.get(ctx.decl(i)) != null)
					var += newTexts.get(ctx.decl(i));
		}
		
		func = "section .text\n"
				+ "global main\n" 
				+ "extern printf\n\n"
				+ func + "\n\n";
		
		var = "section .data\n"
				+ "format db \"%d\", 10, 0\n"
				+ var;
		
		String str = func + var;
		newTexts.put(ctx, str);
		System.out.println(newTexts.get(ctx));
	}	
	
	
	// decl	: var_decl | fun_decl
	@Override
	public void exitDecl(MiniCParser.DeclContext ctx) {
		newTexts.put(ctx, newTexts.get(ctx.getChild(0)));
	}
	
	// stmt	: expr_stmt | compound_stmt | if_stmt | while_stmt | return_stmt
	@Override
	public void exitStmt(MiniCParser.StmtContext ctx) {
		newTexts.put(ctx, newTexts.get(ctx.getChild(0)));
	}
	
	// expr_stmt	: expr ';'
	@Override
	public void exitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
		newTexts.put(ctx, newTexts.get(ctx.getChild(0)));
	}
	
	
	// while_stmt	: WHILE '(' expr ')' stmt
	@Override
	public void exitWhile_stmt(MiniCParser.While_stmtContext ctx) { 
		// Not Implemented
	}
	
	
	@Override
	public void exitFun_decl(MiniCParser.Fun_declContext ctx) {
		// Not Implemented
		String str = "";
		str += getFunName(ctx) + ":\n";
		str += "sub esp, " + symbolTable.getTotalLocalOffset() + "\n";
		str += newTexts.get(ctx.getChild(ctx.getChildCount() - 1));
		str += "add esp, " + symbolTable.getTotalLocalOffset() + "\n";
		str += "ret\n\n";
		
		newTexts.put(ctx, str);
	}
	

	private String funcHeader(MiniCParser.Fun_declContext ctx, String fname) {
		// Not Implemented
		return "";
	}
	
	
	@Override
	public void exitVar_decl(MiniCParser.Var_declContext ctx) {
		String varname = getGlobalVarName(ctx);
		String str = "";
		if(isArrayDecl(ctx)) {
			// Not Implemented
		}
		else if(isDeclWithInit(ctx)) {
			str += varname + " DD " + initVal(ctx) + "\n";
		}
		else {
			str += varname + " DD 0\n";
		}
		
		newTexts.put(ctx, str);
	}

	
	// compound_stmt	: '{' local_decl* stmt* '}'
	@Override
	public void exitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
		String str = "";
		
		for(int i = 0; i < ctx.local_decl().size(); i++)
			str += newTexts.get(ctx.local_decl(i));
		for(int i = 0; i < ctx.stmt().size(); i++)
			str += newTexts.get(ctx.stmt(i));
		
		newTexts.put(ctx, str);
	}
	
	@Override
	public void exitLocal_decl(MiniCParser.Local_declContext ctx) {			
		String varname = getLocalVarName(ctx);
		String str = "";
		if(isArrayDecl(ctx)) {
			// Not Implemented
		}
		else if(isDeclWithInit(ctx)) {
			str += "mov dword [esp + " + symbolTable.getLocalOffset(varname) + "], " + initVal(ctx) + "\n";
		}
		else {
			
		}
		
		newTexts.put(ctx, str);
	}

	// if_stmt	: IF '(' expr ')' stmt | IF '(' expr ')' stmt ELSE stmt;
	@Override
	public void exitIf_stmt(MiniCParser.If_stmtContext ctx) {
		// Not Implemented
	}
	
	
	// return_stmt	: RETURN ';' | RETURN expr ';'
	@Override
	public void exitReturn_stmt(MiniCParser.Return_stmtContext ctx) {
		// Not Implemented
	}

	
	@Override
	public void exitExpr(MiniCParser.ExprContext ctx) {
		String expr = "";

		if(ctx.getChildCount() <= 0) {
			newTexts.put(ctx, ""); 
			return;
		}
		if(ctx.getChildCount() == 1) { // IDENT | LITERAL
			if(ctx.IDENT() != null) {
				String varname = ctx.getChild(0).getText();
				if(symbolTable.isLocalVar(varname))
					expr += "mov eax, dword [esp + " + symbolTable.getLocalOffset(varname) + "]\n";
				else
					expr += "mov eax, [" + varname + "]\n";
			} 
			else if (ctx.LITERAL() != null) {
				expr += "mov eax, " + ctx.LITERAL().getText() + "\n";
			}
		}
		else if(ctx.getChildCount() == 2) { // UnaryOperation
			expr = handleUnaryExpr(ctx, expr);	
		}
		else if(ctx.getChildCount() == 3) {	 
			if(ctx.getChild(0).getText().equals("(")) { 		// '(' expr ')'
				
			} 
			else if(ctx.getChild(1).getText().equals("=")) { 	// IDENT '=' expr
				String varname = ctx.getChild(0).getText();
				expr += newTexts.get(ctx.getChild(2));
				expr += "mov dword [esp + " + symbolTable.getLocalOffset(varname) + "], eax\n";
			} 
			else { 											// binary operation
				expr = handleBinExpr(ctx, expr);
			}
		}
		// IDENT '(' args ')' |  IDENT '[' expr ']'
		else if(ctx.getChildCount() == 4) {
			if(ctx.args() != null){		// function calls
				expr = handleFunCall(ctx, expr);
			} else { // expr
				// Arrays: TODO  
			}
		}
		// IDENT '[' expr ']' '=' expr
		else { // Arrays: TODO			*/
		}
		newTexts.put(ctx, expr);
	}


	private String handleUnaryExpr(MiniCParser.ExprContext ctx, String expr) {
		String l1 = symbolTable.newLabel();
		String l2 = symbolTable.newLabel();
		String lend = symbolTable.newLabel();
		
		expr += newTexts.get(ctx.expr(0));
		switch(ctx.getChild(0).getText()) {
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
			expr += "cmp eax, 0\n"
					+ "je " + l2 + "\n"
					+ l1 + ":\n"
					+ "mov eax, 0\n"
					+ "jmp " + lend + "\n"
					+ l2 + ":\n"
					+ "mov eax, 1\n"
					+ lend + ":\n";
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
				expr += expr2;
				expr += "mov ecx, eax\n";
				expr += expr1;
				expr += "mov edx, 0\n";
				expr += "div ecx\n";
				break;
			case "%":
				expr += expr2;
				expr += "mov ecx, eax\n";
				expr += expr1;
				expr += "mov edx, 0\n";
				expr += "div ecx\n";
				expr += "mov eax, edx\n";
				break;
			case "+":		// expr(0) expr(1) iadd
				expr += expr1;
				expr += "mov ebx, eax\n";
				expr += expr2;
				expr += "add eax, ebx\n";
				break;
			case "-":
				expr += expr2;
				expr += "mov ebx, eax\n";
				expr += expr1;
				expr += "sub eax, ebx\n";
				break;
			case "==":
				expr += expr2;
				expr += "mov ebx, eax\n";
				expr += expr1;
				expr += "cmp eax, ebx\n";
				expr += "je " + l2 + "\n";
				expr += "mov eax, 0\n";
				expr += "jmp " + lend + "\n";
				expr += l2 + ":\n";
				expr += "mov eax, 1\n";
				expr += lend + ":\n";
				break;
			case "!=":
				expr += expr2;
				expr += "mov ebx, eax\n";
				expr += expr1;
				expr += "cmp eax, ebx\n";
				expr += "jne " + l2 + "\n";
				expr += "mov eax, 0\n";
				expr += "jmp " + lend + "\n";
				expr += l2 + ":\n";
				expr += "mov eax, 1\n";
				expr += lend + ":\n";
				break;
			case "<=":
				expr += expr2;
				expr += "mov ebx, eax\n";
				expr += expr1;
				expr += "cmp eax, ebx\n";
				expr += "jle " + l2 + "\n";
				expr += "mov eax, 0\n";
				expr += "jmp " + lend + "\n";
				expr += l2 + ":\n";
				expr += "mov eax, 1\n";
				expr += lend + ":\n";
				break;
			case "<":
				expr += expr2;
				expr += "mov ebx, eax\n";
				expr += expr1;
				expr += "cmp eax, ebx\n";
				expr += "jl " + l2 + "\n";
				expr += "mov eax, 0\n";
				expr += "jmp " + lend + "\n";
				expr += l2 + ":\n";
				expr += "mov eax, 1\n";
				expr += lend + ":\n";
				break;
			case ">=":
				expr += expr2;
				expr += "mov ebx, eax\n";
				expr += expr1;
				expr += "cmp eax, ebx\n";
				expr += "jge " + l2 + "\n";
				expr += "mov eax, 0\n";
				expr += "jmp " + lend + "\n";
				expr += l2 + ":\n";
				expr += "mov eax, 1\n";
				expr += lend + ":\n";
				break;
			case ">":
				expr += expr2;
				expr += "mov ebx, eax\n";
				expr += expr1;
				expr += "cmp eax, ebx\n";
				expr += "jg " + l2 + "\n";
				expr += "mov eax, 0\n";
				expr += "jmp " + lend + "\n";
				expr += l2 + ":\n";
				expr += "mov eax, 1\n";
				expr += lend + ":\n";
				break;
			case "and":
				break;
			case "or":
				break;

		}
		return expr;
	}
	private String handleFunCall(MiniCParser.ExprContext ctx, String expr) {
		String funName = getFunName(ctx);
		if(funName.equals("print_d")) {
			expr = newTexts.get(ctx.args())
					+ "push dword eax\n"
					+ "push dword format\n"
					+ "call printf\n"
					+ "add esp, 8\n";
		}
		return expr;
	}

	// args	: expr (',' expr)* | ;
	@Override
	public void exitArgs(MiniCParser.ArgsContext ctx) {
		String argsStr = "";
		
		for (int i=0; i < ctx.expr().size() ; i++) {
			argsStr += newTexts.get(ctx.expr(i)) ; 
		}		
		newTexts.put(ctx, argsStr);
	}

}
