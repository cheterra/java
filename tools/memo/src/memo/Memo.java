/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package memo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


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
    
    private String filename = "Memo.doc";
    
    
    public Memo(String[] args)
    {
      int mode = M_NL;
      checkInput(args);
      //String name = filename + ".doc";
      try 
      {
        File in = new File(filename);
        in.renameTo(new File(filename + ".bak"));
        in = new File(filename + ".bak");
        BufferedReader inReader = new BufferedReader(new FileReader(in));
        File out = new File(filename);
        FileWriter outWriter = new FileWriter(out);
        // append to archWriter
        FileWriter archWriter = new FileWriter(filename + ".arch.txt", true);
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
        dbgOut("Unexp. " + e);
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
        +  "SYNTAX: java Memo [options] filename \n"
        +  "Example: Memo\n"
        +  "Input: memo.doc\n"
        +  "Output:\n"
        +  "\t memo.doc.bak (backup of the original file)\n"
        +  "\t memo.doc (the new cleaned file)\n"
        +  "\t memo.doc.arch.txt (the archived lines)\n"
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
                filename = args[i];
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