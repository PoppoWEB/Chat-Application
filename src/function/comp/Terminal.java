package function.comp;

import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Terminal {
    private final static Terminal singlton = new Terminal();
    private final static ConcurrentHashMap<Integer ,PrintWriter> text = new ConcurrentHashMap<>();

    private Terminal() {}

    public static Terminal getInstance() {
        return singlton;
    }

    public static void accept(int ID ,PrintWriter writer) {
        text.put( ID, writer );
    }

    public static void messageAll(String line) {
        for (Map.Entry<Integer, PrintWriter> maps : text.entrySet()) {
            maps.getValue().println(line);
        }
    }

    public static void exitMember(int id) {
        text.get(id).close();
        text.remove(id);
    }
}