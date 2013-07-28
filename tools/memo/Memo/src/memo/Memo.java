package memo;

import java.io.*;
import java.util.*;


/**
 * Split memo.doc into new memo.doc and memo-arch.doc (archive file).
 * Remove all lines which ends with OK and all following till next empty line.
 * If memo-arch.doc exists, append lines
 */
public class Memo 
{
    // OK found
    final int M_OK = 1;
    // New empty line found
    final int M_NL = 2;
    
    // newline
    final String NL = System.getProperty("line.separator");
    
    
    public Memo(String[] args)
    {
      int mode = M_NL;
      checkInput(args);
      String filename = "memo.doc";
      try 
      {
        File in = new File(filename);
        in.renameTo(new File("xmemo.bak"));
        in = new File("xmemo.bak");
        BufferedReader inReader = new BufferedReader(new FileReader(in));
        File out = new File(filename);
        FileWriter outWriter = new FileWriter(out);
        // append to archWriter
        FileWriter archWriter = new FileWriter("xmemo-arch.doc", true);
        for (int i = 0; ; i++)
        {
          String line = inReader.readLine();
          if (line == null) 
            break;
          if (line.endsWith("OK"))
            mode = M_OK;
          if (line.length() == 0 || line.equals(NL))
            mode = M_NL;
          line += "\n";
          switch(mode)
          {
            case M_NL: outWriter.write(line); break;
            case M_OK: archWriter.write(line); break;
            default: dbgOut("mode is unknown: " + mode);
          }
        }
        inReader.close();
        outWriter.flush();
        outWriter.close();
        archWriter.flush();
        archWriter.close();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    

    /** Get the options from the command line */
    private void extractOptions(String optionStr)
    {
        String option = optionStr.substring(2, optionStr.length());
        switch(optionStr.charAt(1))
        {
            case 'h':
            case 'H':
            case '?': help(); break;
        }
    }

    /** Show help message */
    private void help()
    {
        dbgOut("main: usage:\n"  
        +  "SYNTAX: java Memo [Options] \n"
        +  "Example: Memo"
        +  "Input: memo.doc, output: memo.bak, memo.doc, memo-arch.doc"
        +  "\n*** Options: ***\n"
        +  "/h\thelp\n"
        );
        System.exit(1);
    }

    /** Test if help needed and loop through arguments */
    public void checkInput(String[] args) 
    {
        if (false && args.length == 0)
        {
            help();
            return;
        }

        /* evaluate options */
        for(int i = 0; i < args.length; i++)
        {
            if(args[i].startsWith("/") ||args[i].startsWith("-"))
            {
                extractOptions(args[i]);
            }
            else // It is not an option, so it is the filename.
            {
                //filename = args[i];
            }
        }
    }
    
    public static void main(String[] args)
    {
        new Memo(args);
    }

    
    /** For debugging */
    private static void dbgOut(String s)
    {
        System.out.println("Memo: " + s);
    }

}