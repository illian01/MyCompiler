package listener.main;

import static listener.main.x86GenListenerHelper.isVoidF;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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

	// params
	static String getParamName(ParamContext param) {
		return param.getChild(1).getText();
	}
	
	static Type getParamType(ParamContext param) {
		if(param.getChild(0).getText().equals("int"))
			return Type.INT;
		
		return null;
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
	static int get_localArraysize(MiniCParser.Local_declContext ctx) {
		return Integer.parseInt(ctx.getChild(3).getText()); 
	}
	static int get_operand_intvalue(MiniCParser.ExprContext ctx) {
		return Integer.parseInt(ctx.getChild(5).getChild(0).getText()); 
	}
	static int get_intarrayindex(MiniCParser.ExprContext ctx) {
		return Integer.parseInt(ctx.getChild(2).getChild(0).getText())*4; 
	}
	static Boolean isNumeric(String str){
		try {
			Integer.parseInt(str);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}
    static String getStringFormat(String str){
	    String[] strip = str.split(" ");
        return strip[0];
    }
    static String tripString(String str){
        String[] split = str.split("\"");
        return split[1];
    }
    
    static String getString_withEscape(String str) {
    	String remove_doublequotes = str.split("\"")[1];
    	List<String> str_array  = new ArrayList();
    	
    	String store = "";
    	String result="";
    	for(int i=0; i<remove_doublequotes.length();i++) {
    		if(remove_doublequotes.charAt(i)=='\\') {
    			if(store.length()!=0)
    			str_array.add(store);
    			String escape = remove_doublequotes.substring(i,i+2);
    			i++;
    			str_array.add(escape);
    			store="";
    		}
    		else{
    			store+=remove_doublequotes.charAt(i);
    		}
    	}
    	
    	for(int i=0; i<str_array.size();i++) {
    		if(str_array.get(i).contains("\\")) {
    			result+=","+get_escape(str_array.get(i))+",";
    		}
    		else {
    			result+="\""+str_array.get(i)+"\"";
    		}
    	}
    	result+=",0";
    	result=result.replaceAll(",,",",");
    	return result;
    }
    
    static String get_escape(String escape) {
    	switch(escape){
    	case "\\n":
    		return "10";
    	case "\\a":
    		return "7";
    	case "\\b":
    		return "8";
    	case "\\f":
    		return "12";
    	case "\\r":
    		return "13";
    	case "\\t":
    		return "9";
    	case "\\v":
    		return "11";
    	case "\\\\":
    		return "92";
    	case "\\'":
    		return "39";
    	case "\\\"":
    		return "34";
    	case "\\?":
    		return "63";
    	default :
    		return null;
    	}
    }

	
}
