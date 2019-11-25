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

import static listener.main.BytecodeGenListenerHelper.*;
import static listener.main.SymbolTable.*;

public class BytecodeGenListener extends MiniCBaseListener implements ParseTreeListener {
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
		// Not Implemented
	}

	
	@Override
	public void exitProgram(MiniCParser.ProgramContext ctx) {
		// Not Implemented
	}	
	
	
	// decl	: var_decl | fun_decl
	@Override
	public void exitDecl(MiniCParser.DeclContext ctx) {
		// Not Implemented
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
		// Not Implemented
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
