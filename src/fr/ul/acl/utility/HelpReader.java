package fr.ul.acl.utility;

import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public final class HelpReader {

    private static final String HELP_PATH = "/help/instructions.html";
    public static JScrollPane helpPane;

    private HelpReader() {
        // Utility class
    }

    public static void loadHelp() {
        try {
            URL helpURL = HelpReader.class.getResource(HELP_PATH);

            if (helpURL == null) {
                throw new IOException("Help file not found: " + HELP_PATH);
            }

            JEditorPane editor = new JEditorPane();
            editor.setContentType("text/html");
            editor.setEditable(false);
            editor.setPage(helpURL);

            helpPane = new JScrollPane(editor);
        } catch(IOException e){
            helpPane = null;
        }
    }
}
