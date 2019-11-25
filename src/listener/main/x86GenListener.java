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
		// Not Implemented
	}
	
	// var_decl	: type_spec IDENT ';' | type_spec IDENT '=' LITERAL ';'|type_spec IDENT '[' LITERAL ']' ';'
	@Override
	public void enterVar_decl(MiniCParser.Var_declContext ctx) {
		// Not Implemented
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
		
		var = "section .data"
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
		// Not Implemented
	}
	
	// expr_stmt	: expr ';'
	@Override
	public void exitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
		// Not Implemented
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
		str += "ret\n";
		
		newTexts.put(ctx, str);
	}
	

	private String funcHeader(MiniCParser.Fun_declContext ctx, String fname) {
		// Not Implemented
		return "";
	}
	
	
	@Override
	public void exitVar_decl(MiniCParser.Var_declContext ctx) {
		// Not Implemented
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
		// Not Implemented
	}


	private String handleUnaryExpr(MiniCParser.ExprContext ctx, String expr) {
		// Not Implemented
		return "";
	}


	private String handleBinExpr(MiniCParser.ExprContext ctx, String expr) {
		// Not Implemented
		return "";
	}
	private String handleFunCall(MiniCParser.ExprContext ctx, String expr) {
		// Not Implemented
		return "";
	}

	// args	: expr (',' expr)* | ;
	@Override
	public void exitArgs(MiniCParser.ArgsContext ctx) {

	}

}
