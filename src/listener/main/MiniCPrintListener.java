package listener.main;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import generated.MiniCBaseListener;
import generated.MiniCParser;

public class MiniCPrintListener extends MiniCBaseListener {
	ParseTreeProperty<String> newTexts = new ParseTreeProperty<>();
	
	boolean isSimpleDecl(MiniCParser.Var_declContext ctx) {
		return ctx.getChildCount() == 3;
	}
	
	boolean isDeclAndInit(MiniCParser.Var_declContext ctx) {
		return ctx.getChildCount() == 5;
	}
	
	boolean isArrayDecl(MiniCParser.Var_declContext ctx) {
		return ctx.getChildCount() == 6;
	}
	
	boolean isSimpleParam(MiniCParser.ParamContext ctx) {
		return ctx.getChildCount() == 2;
	}
	
	boolean isArrayParam(MiniCParser.ParamContext ctx) {
		return ctx.getChildCount() == 4;
	}
	
	boolean isParamsEmpty(MiniCParser.ParamsContext ctx) {
		return ctx.getChildCount() == 0;
	}
	
	boolean isParamsVoid(MiniCParser.ParamsContext ctx) {
		return ctx.getChild(0).getText() == "VOID";
	}
	
	boolean isParamsExist(MiniCParser.ParamsContext ctx) {
		return newTexts.get(ctx.getChild(0)) != null;
	}
	
	boolean isSimpleDeclLocal(MiniCParser.Local_declContext ctx) {
		return ctx.getChildCount() == 3;
	}
	
	boolean isDeclAndInitLocal(MiniCParser.Local_declContext ctx) {
		return ctx.getChildCount() == 5;
	}
	
	boolean isArrayDeclLocal(MiniCParser.Local_declContext ctx) {
		return ctx.getChildCount() == 6;
	}
	
	boolean isIfstmt(MiniCParser.If_stmtContext ctx) {
		return ctx.getChildCount() == 5;
	}
	
	boolean isIfElsestmt(MiniCParser.If_stmtContext ctx) {
		return ctx.getChildCount() == 7;
	}
	
	boolean isArrayAssignExpr(MiniCParser.ExprContext ctx) {
		return ctx.getChildCount() == 6;
	}
	
	boolean isAssignExpr(MiniCParser.ExprContext ctx) {
		return ctx.getChildCount() == 3 && ctx.getChild(0) == ctx.IDENT();
	}
	
	boolean isBinaryOperationExpr(MiniCParser.ExprContext ctx) {
		return ctx.getChildCount() == 3 && ctx.getChild(1) != ctx.expr();
	}
	
	boolean isUnaryOperationExpr(MiniCParser.ExprContext ctx) {
		return ctx.getChildCount() == 2 && ctx.getChild(0) != ctx.expr();
	}
	
	boolean isIDBracketsExpr(MiniCParser.ExprContext ctx) {
		return ctx.getChildCount() == 4 && ctx.getChild(2) == ctx.args();
	}
	
	boolean isIDSquareBracketsExpr(MiniCParser.ExprContext ctx) {
		return ctx.getChildCount() == 4 && ctx.getChild(2) == ctx.expr(0);
	}
	
	boolean isBracketsExpr(MiniCParser.ExprContext ctx) {
		return ctx.getChildCount() == 3 && ctx.getChild(0).getText() == "(";
	}
	
	boolean isIDOrLiteralExpr(MiniCParser.ExprContext ctx) {
		return ctx.getChildCount() == 1;
	}
	
	boolean isArgsEmpty(MiniCParser.ArgsContext ctx) {
		return ctx.getChildCount() == 0;
	}
	
	boolean isSimpleReturn(MiniCParser.Return_stmtContext ctx) {
		return ctx.getChildCount() == 2;
	}
	
	boolean isReturnexpr(MiniCParser.Return_stmtContext ctx) {
		return ctx.getChildCount() == 3;
	}
	
	
	
	
	@Override 
	public void exitWhile_stmt(MiniCParser.While_stmtContext ctx) {
		String s = ctx.getChild(0).getText() + " (" + newTexts.get(ctx.getChild(2))
					+ ") " + newTexts.get(ctx.getChild(4));
		newTexts.put(ctx, s);
	}

	@Override
	public void exitDecl(MiniCParser.DeclContext ctx) {
		newTexts.put(ctx, newTexts.get(ctx.getChild(0)));
	}
	
	@Override 
	public void exitFun_decl(MiniCParser.Fun_declContext ctx) {
		String s = newTexts.get(ctx.getChild(0)) + " " + ctx.getChild(1).getText()
				+ "(" +  newTexts.get(ctx.getChild(3)) + ")" + newTexts.get(ctx.getChild(5));
		newTexts.put(ctx, s);
	}

	@Override
	public void exitIf_stmt(MiniCParser.If_stmtContext ctx) { 
		String s = ctx.getChild(0).getText() + " (";
		s += newTexts.get(ctx.getChild(2)) + ") ";
		s += newTexts.get(ctx.getChild(4));
		if(isIfstmt(ctx))
			newTexts.put(ctx, s);
		else if(isIfElsestmt(ctx))
			newTexts.put(ctx, s + ctx.getChild(5).getText() + " " + newTexts.get(ctx.getChild(6)));
	}
	
	@Override 
	public void exitProgram(MiniCParser.ProgramContext ctx) { 
		for(ParseTree i : ctx.children)
			System.out.println(newTexts.get(i));
	}
	
	@Override 
	public void exitParams(MiniCParser.ParamsContext ctx) {
		
		if(isParamsEmpty(ctx)) {
			newTexts.put(ctx, "");
		}
		else if(isParamsVoid(ctx)) {
			newTexts.put(ctx, ctx.getChild(0).getText());
		}
		else if(isParamsExist(ctx)) {
			String s = newTexts.get(ctx.getChild(0));
			for(int i = 2; i < ctx.getChildCount(); i+=2)
				s += ", " + newTexts.get(ctx.getChild(i));
			newTexts.put(ctx, s);
		}
	}
	
	@Override 
	public void exitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
		newTexts.put(ctx, newTexts.get(ctx.getChild(0)) + ";");
	}
	
	@Override 
	public void exitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
		String indent = "";
		String braceIndent = "";
		for(int i = 0; i < ctx.depth(); i++)
			indent += ".";
		for(int i = 0; i < ctx.depth() - 4; i++)
			braceIndent += ".";
		
		String s = "\n" + braceIndent + "{\n";
		for(int i = 0; i < ctx.getChildCount()-2; i++)
			s += indent + newTexts.get(ctx.getChild(i+1)) + "\n";
		s += braceIndent + "}";
		newTexts.put(ctx, s);
	}
	
	@Override 
	public void exitArgs(MiniCParser.ArgsContext ctx) {
		if(isArgsEmpty(ctx)) {
			newTexts.put(ctx, "");
		}
		else {
			String s = newTexts.get(ctx.getChild(0));
			for(int i = 2; i < ctx.getChildCount(); i+=2)
				s += ", " + newTexts.get(ctx.getChild(i));
			newTexts.put(ctx, s);
		}
	}
	
	@Override 
	public void exitLocal_decl(MiniCParser.Local_declContext ctx) {
		String s = newTexts.get(ctx.getChild(0)) + " " + ctx.getChild(1).getText();
		if(isSimpleDeclLocal(ctx))
			newTexts.put(ctx, s + ";");
		else if(isDeclAndInitLocal(ctx))
			newTexts.put(ctx, s + " = " + ctx.getChild(3) + ";");
		else if(isArrayDeclLocal(ctx))
			newTexts.put(ctx, s + "[" + ctx.getChild(3) + "];");
	}
	
	@Override 
	public void exitType_spec(MiniCParser.Type_specContext ctx) { 
		newTexts.put(ctx, ctx.getChild(0).getText());
	}
	
	@Override
	public void exitParam(MiniCParser.ParamContext ctx) {
		String s = newTexts.get(ctx.getChild(0)) + " " +ctx.getChild(1).getText();
		if(isSimpleParam(ctx))
			newTexts.put(ctx, s);
		if(isArrayParam(ctx))
			newTexts.put(ctx, s + "[]");
	}
	
	@Override
	public void exitExpr(MiniCParser.ExprContext ctx) {
		if(isArrayAssignExpr(ctx))
			newTexts.put(ctx, ctx.getChild(0).getText() + "[" + newTexts.get(ctx.getChild(2)) + "] = " + newTexts.get(ctx.getChild(5)));
		else if(isAssignExpr(ctx))
			newTexts.put(ctx, ctx.getChild(0).getText() + " = " + newTexts.get(ctx.getChild(2)));
		else if(isBinaryOperationExpr(ctx))
			newTexts.put(ctx, newTexts.get(ctx.getChild(0)) + " " +ctx.getChild(1).getText() + " " + newTexts.get(ctx.getChild(2)));
		else if(isUnaryOperationExpr(ctx))
			newTexts.put(ctx, ctx.getChild(0).getText() + newTexts.get(ctx.getChild(1)));
		else if(isIDBracketsExpr(ctx))
			newTexts.put(ctx, ctx.getChild(0).getText() + "(" + newTexts.get(ctx.getChild(2)) + ")");
		else if(isIDSquareBracketsExpr(ctx))
			newTexts.put(ctx, ctx.getChild(0).getText() + "[" + newTexts.get(ctx.getChild(2)) + "]");
		else if(isBracketsExpr(ctx))
			newTexts.put(ctx, "(" + newTexts.get(ctx.getChild(1)) + ")");
		else if(isIDOrLiteralExpr(ctx))
			newTexts.put(ctx, ctx.getChild(0).getText());
	}
	
	@Override 
	public void exitVar_decl(MiniCParser.Var_declContext ctx) {
		String s = newTexts.get(ctx.getChild(0)) + " " + ctx.getChild(1).getText();
		if(isSimpleDecl(ctx))
			newTexts.put(ctx, s + ";");
		else if(isDeclAndInit(ctx))
			newTexts.put(ctx, s + " = " + ctx.getChild(3) + ";");
		else if(isArrayDecl(ctx))
			newTexts.put(ctx, s + "[" + ctx.getChild(3) + "];");
	}

	@Override 
	public void exitStmt(MiniCParser.StmtContext ctx) {
		newTexts.put(ctx, newTexts.get(ctx.getChild(0)));
	}
	
}
