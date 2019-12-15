package listener.main;

import java.util.HashMap;
import java.util.Map;

import generated.MiniCParser;
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
	private Map<String, String> _ssymtable = new HashMap<>();	// function

	private int _localOffset = 0;
	private int _argOffset = 8;

	private int _globalVarID = 0;
	private int _localVarID = 0;
	private int _labelID = 0;
	private int _stringID = 0;
	private int _argVarID = 0;

	SymbolTable(){
		initFunDecl();
	}

	void initFunDecl(){		// at each func decl
		_lsymtable.clear();
		_localVarID = 0;
		_argVarID = 0;
		_localOffset = 0;
		_argOffset = 8;
	}
	void putLocalArray(String varname, Type type,int size) {
		_localOffset += 4*size;
		this._lsymtable.put(varname, new VarInfo(type, _localVarID++, _localOffset));
	}
	void putglobalArray(String varname, Type type){
		this._gsymtable.put(varname, new VarInfo(type, _globalVarID++));
	}
	void putLocalVar(String varname, Type type){
		_localOffset += 4;
		this._lsymtable.put(varname, new VarInfo(type, _localVarID++, _localOffset));

	}
	void putString(String str){
		//this._ssymtable.put(str,"format" + _stringID++  + " db \"" + tripString(str) + "\", 0\n");
		if( !this._ssymtable.containsKey(str) )
			this._ssymtable.put(str,"format" + _stringID++  + " db " + str + "\n");
	}

	Map<String, String> getStringTable(){
		return this._ssymtable;
	}

	String getString(String str){
			return this._ssymtable.get(str);
	}

	void putLocalVarWithInitVal(String varname, Type type, int initVar){
		_localOffset += 4;
		this._lsymtable.put(varname, new VarInfo(type, _localVarID++, initVar, _localOffset));

	}
	void putGlobalVarWithInitVal(String varname, Type type, int initVar){
		this._gsymtable.put(varname, new VarInfo(type, _globalVarID++, initVar));
	}

	void putParams(MiniCParser.ParamsContext params) {
		for(int i = 0; i < params.param().size(); i++) {
			String varname = getParamName(params.param(i));
			Type type = getParamType(params.param(i));
			this._lsymtable.put(varname, new VarInfo(type, _argVarID++, -1*_argOffset));
			this._argOffset += 4;
		}
	}

	String newLabel() {
		return "label" + _labelID++;
	}

	// local
	public int getTotalLocalOffset() {
		return _localOffset;
	}
	public int getTotalargsOffset() {
		return _argVarID*4;
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
	public boolean is_existVar(String varname) {
		if(isLocalVar(varname) || isglobalVar(varname)) {
			return true;
		}
		return false;
	}
}
