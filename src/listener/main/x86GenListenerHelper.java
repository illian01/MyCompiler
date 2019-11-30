package listener.main;

import static listener.main.x86GenListenerHelper.isIntF;
import static listener.main.x86GenListenerHelper.isVoidF;

import java.util.Hashtable;

import generated.MiniCParser;
import generated.MiniCParser.ExprContext;
import generated.MiniCParser.Fun_declContext;
import generated.MiniCParser.If_stmtContext;
import generated.MiniCParser.Local_declContext;
import generated.MiniCParser.ParamContext;
import generated.MiniCParser.ParamsContext;
import generated.MiniCParser.Type_specContext;
import generated.MiniCParser.Var_declContext;
import listener.main.SymbolTable;
import listener.main.SymbolTable.Type;
import listener.main.SymbolTable.VarInfo;

public class x86GenListenerHelper {
	
	// <boolean functions>
	
	static boolean isFunDecl(MiniCParser.ProgramContext ctx, int i) {
		return ctx.getChild(i).getChild(0) instanceof MiniCParser.Fun_declContext;
	}
	
	// type_spec IDENT '[' ']'
	static boolean isArrayParamDecl(ParamContext param) {
		return param.getChildCount() == 4;
	}
	
	// global vars
	static int initVal(Var_declContext ctx) {
		return Integer.parseInt(ctx.LITERAL().getText());
	}
	static int initsize(Var_declContext ctx) {
		return Integer.parseInt(ctx.getChild(3).getText());
	}
	// var_decl	: type_spec IDENT '=' LITERAL ';
	static boolean isDeclWithInit(Var_declContext ctx) {
		return ctx.getChildCount() == 5 ;
	}
	// var_decl	: type_spec IDENT '[' LITERAL ']' ';'
	static boolean isArrayDecl(Var_declContext ctx) {
		return ctx.getChildCount() == 6;
	}

	// <local vars>
	// local_decl	: type_spec IDENT '[' LITERAL ']' ';'
	static int initVal(Local_declContext ctx) {
		return Integer.parseInt(ctx.LITERAL().getText());
	}

	static boolean isArrayDecl(Local_declContext ctx) {
		return ctx.getChildCount() == 6;
	}
	
	static boolean isDeclWithInit(Local_declContext ctx) {
		return ctx.getChildCount() == 5 ;
	}
	
	static boolean isVoidF(Fun_declContext ctx) {
		return ctx.getChild(0).getText().equals("void");
	}
	
	static boolean isIntF(Fun_declContext ctx) {
		return ctx.getChild(0).getText().equals("int");
	}
	
	static boolean isIntReturn(MiniCParser.Return_stmtContext ctx) {
		return ctx.getChildCount() == 3;
	}

	static boolean isVoidReturn(MiniCParser.Return_stmtContext ctx) {
		return ctx.getChildCount() == 2;
	}
	
	// <information extraction>
	static String getStackSize(Fun_declContext ctx) {
		return "32";
	}
	static String getLocalVarSize(Fun_declContext ctx) {
		return "32";
	}
	static String getTypeText(Type_specContext typespec) {
		String type = typespec.getText(); 
		if(type.compareTo("int") == 0)
			return "I";
		
		return "";
	}
	
	static String getFReturnType(Fun_declContext ctx) {
		if(isIntF(ctx))
			return "I";
		if(isVoidF(ctx))
			return "V";
		
		return "";
	}

	// params
	static String getParamName(ParamContext param) {
		return param.getChild(1).getText();
	}
	
	static Type getParamType(ParamContext param) {
		if(param.getChild(0).getText().equals("int"))
			return Type.INT;
		
		return null;
	}
	
	static String getParamTypesText(ParamsContext params) {
		String typeText = "";
		
		for(int i = 0; i < params.param().size(); i++) {
			MiniCParser.Type_specContext typespec = (MiniCParser.Type_specContext)  params.param(i).getChild(0);
			typeText += getTypeText(typespec); // + ";";
		}
		return typeText;
	}
	
	static String getLocalVarName(Local_declContext local_decl) {
		return local_decl.getChild(1).getText();
	}
	
	static String getGlobalVarName(MiniCParser.Var_declContext global_decl) {
		return global_decl.getChild(1).getText();
	}
	
	static String getFunName(Fun_declContext ctx) {
		return ctx.getChild(1).getText();
	}
	
	static String getFunName(ExprContext ctx) {
		return ctx.getChild(0).getText();
	}
	
	static boolean noElse(If_stmtContext ctx) {
		return ctx.getChildCount() <= 5;
	}
	
	static String getFunProlog() {
		String prologue = "";
		
		prologue += ".class public Test\n";
		prologue += ".super java/lang/Object\n\n";
		
		prologue += ".method public <init>()V\n";
		prologue += "aload_0\n";
		prologue += "invokenonvirtual java/lang/Object/<init>()V\n";
		prologue += "return\n";
		prologue += ".end method\n\n";
		
		return prologue;
	}
	
	static String getCurrentClassName() {
		return "Test";
	}
	static int get_localArraysize(MiniCParser.Local_declContext ctx) {
		return Integer.parseInt(ctx.getChild(3).getText()); 
	}
	static int get_operand(MiniCParser.ExprContext ctx) {
		return Integer.parseInt(ctx.getChild(5).getChild(0).getText()); 
	}
	static int get_intarrayindex(MiniCParser.ExprContext ctx) {
		return Integer.parseInt(ctx.getChild(2).getChild(0).getText())*4; 
	}
	static int get_globalarrayindex(MiniCParser.ExprContext ctx) {
		return Integer.parseInt(ctx.getChild(2).getChild(0).getText());
	}
	static Boolean isNumeric(String str){
		try {
			Integer.parseInt(str);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}

	
}
