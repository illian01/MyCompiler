package listener.main;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import generated.*;

public class Translator {
	enum OPTIONS {
		PRETTYPRINT, X86, ERROR
	}
	private static OPTIONS getOption(String[] args){
		if (args.length < 1)
			return OPTIONS.X86;
		
		if (args[0].startsWith("-p") 
				|| args[0].startsWith("-P"))
			return OPTIONS.PRETTYPRINT;
		
		if (args[0].startsWith("-b") 
				|| args[0].startsWith("-B"))
			return OPTIONS.X86;

		return OPTIONS.ERROR;
	}
	
	public static void main(String[] args) throws Exception
	{
		CharStream codeCharStream = CharStreams.fromFileName("test.c");
		MiniCLexer lexer = new MiniCLexer(codeCharStream);
		CommonTokenStream tokens = new CommonTokenStream( lexer );
		MiniCParser parser = new MiniCParser( tokens );
		ParseTree tree = parser.program();
		
		ParseTreeWalker walker = new ParseTreeWalker();
		switch (getOption(args)) {
			case PRETTYPRINT : 		
				walker.walk(new MiniCPrintListener(), tree);
				break;
			case X86:
				walker.walk(new x86GenListener(), tree );
				break;
			default:
				break;
		}
	}
}