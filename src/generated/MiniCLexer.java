// Generated from MiniC.g4 by ANTLR 4.7.2
 
package generated;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MiniCLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, VOID=20, INT=21, WHILE=22, IF=23, ELSE=24, RETURN=25, 
		OR=26, AND=27, LE=28, GE=29, EQ=30, NE=31, IDENT=32, STRING=33, LITERAL=34, 
		DecimalConstant=35, OctalConstant=36, HexadecimalConstant=37, WS=38;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
			"T__17", "T__18", "VOID", "INT", "WHILE", "IF", "ELSE", "RETURN", "OR", 
			"AND", "LE", "GE", "EQ", "NE", "IDENT", "STRING", "LITERAL", "DecimalConstant", 
			"OctalConstant", "HexadecimalConstant", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'='", "'['", "']'", "'('", "')'", "','", "'{'", "'}'", 
			"'-'", "'+'", "'--'", "'++'", "'*'", "'/'", "'%'", "'<'", "'>'", "'!'", 
			"'void'", "'int'", "'while'", "'if'", "'else'", "'return'", "'or'", "'and'", 
			"'<='", "'>='", "'=='", "'!='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, "VOID", "INT", "WHILE", 
			"IF", "ELSE", "RETURN", "OR", "AND", "LE", "GE", "EQ", "NE", "IDENT", 
			"STRING", "LITERAL", "DecimalConstant", "OctalConstant", "HexadecimalConstant", 
			"WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public MiniCLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "MiniC.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2(\u00de\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\3\2\3\2\3\3\3\3\3\4\3\4\3"+
		"\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3"+
		"\r\3\r\3\16\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23"+
		"\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\27\3\27\3\27"+
		"\3\27\3\27\3\27\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\35\3\35\3\35"+
		"\3\36\3\36\3\36\3\37\3\37\3\37\3 \3 \3 \3!\3!\7!\u00ab\n!\f!\16!\u00ae"+
		"\13!\3\"\3\"\3\"\3\"\7\"\u00b4\n\"\f\"\16\"\u00b7\13\"\3\"\3\"\3#\3#\3"+
		"#\5#\u00be\n#\3$\3$\3$\7$\u00c3\n$\f$\16$\u00c6\13$\5$\u00c8\n$\3%\3%"+
		"\7%\u00cc\n%\f%\16%\u00cf\13%\3&\3&\3&\6&\u00d4\n&\r&\16&\u00d5\3\'\6"+
		"\'\u00d9\n\'\r\'\16\'\u00da\3\'\3\'\2\2(\3\3\5\4\7\5\t\6\13\7\r\b\17\t"+
		"\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27"+
		"-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(\3\2\13\5"+
		"\2C\\aac|\6\2\62;C\\aac|\3\2$$\3\2\63;\3\2\62;\3\2\629\4\2ZZzz\5\2\62"+
		";CHch\5\2\13\f\17\17\"\"\2\u00e7\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2"+
		"\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2"+
		"\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2"+
		"\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2"+
		"\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2"+
		"\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2"+
		"\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\3O"+
		"\3\2\2\2\5Q\3\2\2\2\7S\3\2\2\2\tU\3\2\2\2\13W\3\2\2\2\rY\3\2\2\2\17[\3"+
		"\2\2\2\21]\3\2\2\2\23_\3\2\2\2\25a\3\2\2\2\27c\3\2\2\2\31e\3\2\2\2\33"+
		"h\3\2\2\2\35k\3\2\2\2\37m\3\2\2\2!o\3\2\2\2#q\3\2\2\2%s\3\2\2\2\'u\3\2"+
		"\2\2)w\3\2\2\2+|\3\2\2\2-\u0080\3\2\2\2/\u0086\3\2\2\2\61\u0089\3\2\2"+
		"\2\63\u008e\3\2\2\2\65\u0095\3\2\2\2\67\u0098\3\2\2\29\u009c\3\2\2\2;"+
		"\u009f\3\2\2\2=\u00a2\3\2\2\2?\u00a5\3\2\2\2A\u00a8\3\2\2\2C\u00af\3\2"+
		"\2\2E\u00bd\3\2\2\2G\u00c7\3\2\2\2I\u00c9\3\2\2\2K\u00d0\3\2\2\2M\u00d8"+
		"\3\2\2\2OP\7=\2\2P\4\3\2\2\2QR\7?\2\2R\6\3\2\2\2ST\7]\2\2T\b\3\2\2\2U"+
		"V\7_\2\2V\n\3\2\2\2WX\7*\2\2X\f\3\2\2\2YZ\7+\2\2Z\16\3\2\2\2[\\\7.\2\2"+
		"\\\20\3\2\2\2]^\7}\2\2^\22\3\2\2\2_`\7\177\2\2`\24\3\2\2\2ab\7/\2\2b\26"+
		"\3\2\2\2cd\7-\2\2d\30\3\2\2\2ef\7/\2\2fg\7/\2\2g\32\3\2\2\2hi\7-\2\2i"+
		"j\7-\2\2j\34\3\2\2\2kl\7,\2\2l\36\3\2\2\2mn\7\61\2\2n \3\2\2\2op\7\'\2"+
		"\2p\"\3\2\2\2qr\7>\2\2r$\3\2\2\2st\7@\2\2t&\3\2\2\2uv\7#\2\2v(\3\2\2\2"+
		"wx\7x\2\2xy\7q\2\2yz\7k\2\2z{\7f\2\2{*\3\2\2\2|}\7k\2\2}~\7p\2\2~\177"+
		"\7v\2\2\177,\3\2\2\2\u0080\u0081\7y\2\2\u0081\u0082\7j\2\2\u0082\u0083"+
		"\7k\2\2\u0083\u0084\7n\2\2\u0084\u0085\7g\2\2\u0085.\3\2\2\2\u0086\u0087"+
		"\7k\2\2\u0087\u0088\7h\2\2\u0088\60\3\2\2\2\u0089\u008a\7g\2\2\u008a\u008b"+
		"\7n\2\2\u008b\u008c\7u\2\2\u008c\u008d\7g\2\2\u008d\62\3\2\2\2\u008e\u008f"+
		"\7t\2\2\u008f\u0090\7g\2\2\u0090\u0091\7v\2\2\u0091\u0092\7w\2\2\u0092"+
		"\u0093\7t\2\2\u0093\u0094\7p\2\2\u0094\64\3\2\2\2\u0095\u0096\7q\2\2\u0096"+
		"\u0097\7t\2\2\u0097\66\3\2\2\2\u0098\u0099\7c\2\2\u0099\u009a\7p\2\2\u009a"+
		"\u009b\7f\2\2\u009b8\3\2\2\2\u009c\u009d\7>\2\2\u009d\u009e\7?\2\2\u009e"+
		":\3\2\2\2\u009f\u00a0\7@\2\2\u00a0\u00a1\7?\2\2\u00a1<\3\2\2\2\u00a2\u00a3"+
		"\7?\2\2\u00a3\u00a4\7?\2\2\u00a4>\3\2\2\2\u00a5\u00a6\7#\2\2\u00a6\u00a7"+
		"\7?\2\2\u00a7@\3\2\2\2\u00a8\u00ac\t\2\2\2\u00a9\u00ab\t\3\2\2\u00aa\u00a9"+
		"\3\2\2\2\u00ab\u00ae\3\2\2\2\u00ac\u00aa\3\2\2\2\u00ac\u00ad\3\2\2\2\u00ad"+
		"B\3\2\2\2\u00ae\u00ac\3\2\2\2\u00af\u00b5\7$\2\2\u00b0\u00b4\n\4\2\2\u00b1"+
		"\u00b2\7^\2\2\u00b2\u00b4\7$\2\2\u00b3\u00b0\3\2\2\2\u00b3\u00b1\3\2\2"+
		"\2\u00b4\u00b7\3\2\2\2\u00b5\u00b3\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b8"+
		"\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b8\u00b9\7$\2\2\u00b9D\3\2\2\2\u00ba\u00be"+
		"\5G$\2\u00bb\u00be\5I%\2\u00bc\u00be\5K&\2\u00bd\u00ba\3\2\2\2\u00bd\u00bb"+
		"\3\2\2\2\u00bd\u00bc\3\2\2\2\u00beF\3\2\2\2\u00bf\u00c8\7\62\2\2\u00c0"+
		"\u00c4\t\5\2\2\u00c1\u00c3\t\6\2\2\u00c2\u00c1\3\2\2\2\u00c3\u00c6\3\2"+
		"\2\2\u00c4\u00c2\3\2\2\2\u00c4\u00c5\3\2\2\2\u00c5\u00c8\3\2\2\2\u00c6"+
		"\u00c4\3\2\2\2\u00c7\u00bf\3\2\2\2\u00c7\u00c0\3\2\2\2\u00c8H\3\2\2\2"+
		"\u00c9\u00cd\7\62\2\2\u00ca\u00cc\t\7\2\2\u00cb\u00ca\3\2\2\2\u00cc\u00cf"+
		"\3\2\2\2\u00cd\u00cb\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ceJ\3\2\2\2\u00cf"+
		"\u00cd\3\2\2\2\u00d0\u00d1\7\62\2\2\u00d1\u00d3\t\b\2\2\u00d2\u00d4\t"+
		"\t\2\2\u00d3\u00d2\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5\u00d3\3\2\2\2\u00d5"+
		"\u00d6\3\2\2\2\u00d6L\3\2\2\2\u00d7\u00d9\t\n\2\2\u00d8\u00d7\3\2\2\2"+
		"\u00d9\u00da\3\2\2\2\u00da\u00d8\3\2\2\2\u00da\u00db\3\2\2\2\u00db\u00dc"+
		"\3\2\2\2\u00dc\u00dd\b\'\2\2\u00ddN\3\2\2\2\r\2\u00aa\u00ac\u00b3\u00b5"+
		"\u00bd\u00c4\u00c7\u00cd\u00d5\u00da\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}