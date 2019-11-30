package listener.main;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.plaf.synth.SynthSplitPaneUI;

import generated.MiniCParser;
import generated.MiniCParser.Fun_declContext;
import generated.MiniCParser.Local_declContext;
import generated.MiniCParser.ParamsContext;
import generated.MiniCParser.Type_specContext;
import generated.MiniCParser.Var_declContext;
import listener.main.SymbolTable.Type;
import static listener.main.x86GenListenerHelper.*;


public class SymbolTable {
	enum Type {
		INT, INTARRAY, VOID, ERROR
	}
	
	static public class VarInfo {
		Type type; 
		int id;
		int initVal;
		int offset;
		
		public VarInfo(Type type,  int id, int initVal, int offset) {
			this.type = type;
			this.id = id;
			this.initVal = initVal;
			this.offset = offset;
		}
		public VarInfo(Type type,  int id, int offset) {
			this.type = type;
			this.id = id;
			this.initVal = 0;
			this.offset = offset;
		}
		public VarInfo(Type type,  int id) {
			this.type = type;
			this.id = id;
			this.initVal = 0;
		}
	}
	
	static public class FInfo {
		public String sigStr;
	}
	
	private Map<String, VarInfo> _lsymtable = new HashMap<>();	// local v.
	private Map<String, VarInfo> _asymtable = new HashMap<>();	// local v.
	private Map<String, VarInfo> _gsymtable = new HashMap<>();	// global v.
	private Map<String, FInfo> _fsymtable = new HashMap<>();	// function 
	
	private int _localOffset = 0;
	private int _argOffset = 8;
		
	private int _globalVarID = 0;
	private int _localVarID = 0;
	private int _labelID = 0;
	private int _argVarID = 0;
	
	SymbolTable(){
		initFunDecl();
		initFunTable();
	}
	
	void initFunDecl(){		// at each func decl
		_lsymtable.clear();
		_localVarID = 0;
		_labelID = 0;
		_argVarID = 0;
		_localOffset = 0;
	}
	void putLocalArray(String varname, Type type,int size) {
		this._lsymtable.put(varname, new VarInfo(type, _localVarID++, _localOffset));
		_localOffset += 4*size;
	}
	void putglobalArray(String varname, Type type){
		this._gsymtable.put(varname, new VarInfo(type, _globalVarID++));
	}
	void putLocalVar(String varname, Type type){
		this._lsymtable.put(varname, new VarInfo(type, _localVarID++, _localOffset));
		_localOffset += 4;
	}
	
	void putargVar(String varname, Type type){
		this._asymtable.put(varname, new VarInfo(type, _argVarID++, _argOffset));
		_argOffset += 4;
	}
	
	void putGlobalVar(String varname, Type type){
		this._gsymtable.put(varname, new VarInfo(type, _globalVarID++));
	}
	
	void putLocalVarWithInitVal(String varname, Type type, int initVar){
		this._lsymtable.put(varname, new VarInfo(type, _localVarID++, initVar, _localOffset));
		_localOffset += 4;
	}
	void putGlobalVarWithInitVal(String varname, Type type, int initVar){
		this._gsymtable.put(varname, new VarInfo(type, _globalVarID++, initVar));
	}

	void putParams(MiniCParser.ParamsContext params) {
		for(int i = 0; i < params.param().size(); i++) {
			String varname = getParamName(params.param(i));
			Type type = getParamType(params.param(i));
			this._asymtable.put(varname, new VarInfo(type, _argVarID++, _argOffset));
			this._argOffset += 4;
		}
	}
	
	private void initFunTable() {
		FInfo printlninfo = new FInfo();
		printlninfo.sigStr = "java/io/PrintStream/println(I)V";
		
		FInfo maininfo = new FInfo();
		maininfo.sigStr = "main([Ljava/lang/String;)V";
		_fsymtable.put("_print", printlninfo);
		_fsymtable.put("main", maininfo);
	}
	
	public String getFunSpecStr(String fname) {		
		return _fsymtable.get(fname).sigStr;
	}

	public String getFunSpecStr(Fun_declContext ctx) {
		return getFunSpecStr(ctx.getChild(1).getText());
	}
	
	public String putFunSpecStr(Fun_declContext ctx) {
		String fname = getFunName(ctx);
		String argtype = "";	
		String rtype = "";
		String res = "";
		
		rtype = getFReturnType(ctx);
		
		ParamsContext params = (MiniCParser.ParamsContext) ctx.getChild(3);
		argtype = getParamTypesText(params);
		
		res =  fname + "(" + argtype + ")" + rtype;

		FInfo finfo = new FInfo();
		finfo.sigStr = res;
		_fsymtable.put(fname, finfo);
		
		return res;
	}
	
	String getVarId(String name){
		if(_lsymtable.containsKey(name))
			return Integer.toString(_lsymtable.get(name).id);
		else if(_gsymtable.containsKey(name))
			return Integer.toString(_gsymtable.get(name).id);
		return "";
	}
	
	Type getVarType(String name){
		VarInfo lvar = (VarInfo) _lsymtable.get(name);
		if (lvar != null) {
			return lvar.type;
		}
		
		VarInfo gvar = (VarInfo) _gsymtable.get(name);
		if (gvar != null) {
			return gvar.type;
		}
		
		return Type.ERROR;	
	}
	String newLabel() {
		return "label" + _labelID++;
	}
	
	// global
	public String getVarId(Var_declContext ctx) {
		// <Fill here>
		String sname = "";
		return null;
	}

	// local
	public String getVarId(Local_declContext ctx) {
		String sname = "";
		sname += getVarId(ctx.IDENT().getText());
		return sname;
	}
	
	public int getTotalLocalOffset() {
		return _localOffset;
	}
	
	public int getLocalOffset(String varname) {
		return _lsymtable.get(varname).offset;
	}
	public int getargOffset(String varname) {
		return _asymtable.get(varname).offset;
	}
	
	public boolean isLocalVar(String varname) {
		return _lsymtable.containsKey(varname);
	}
	public boolean isargVar(String varname) {
		return _asymtable.containsKey(varname);
	}
	public boolean isglobalVar(String varname) {
		return _gsymtable.containsKey(varname);
	}
	public boolean isglobalArray(MiniCParser.ExprContext ctx) {
		
		if (isglobalVar(ctx.getParent().getChild(0).getText())&&ctx.getParent().getChildCount()>=5) {
			return true;
		}
		return false;
	}
	public boolean iseachVar(String varname) {
		if(isLocalVar(varname) || isglobalVar(varname)) {
			return true;
		}
		return false;
	}
}
