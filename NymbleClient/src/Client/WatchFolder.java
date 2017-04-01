/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

/**
 *
 * @author Inbo
 */
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;

public class WatchFolder {

    static boolean stop = false;

    static <T> WatchEvent<T> castEvent(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    public WatchFolder(String path) throws Exception {
        Path _directotyToWatch = Paths.get(path);
        WatchService watcherSvc = FileSystems.getDefault().newWatchService();
        WatchKey watchKey = _directotyToWatch.register(watcherSvc, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

        while (true) {
            watchKey = watcherSvc.take();
            for (WatchEvent<?> event : watchKey.pollEvents()) {
                WatchEvent<Path> watchEvent = castEvent(event);
                System.out.println(event.kind().name().toString() + " " + _directotyToWatch.resolve(watchEvent.context()));
                watchKey.reset();
                if (event.kind().name().equals("ENTRY_DELETE")) {
//                    System.out.println("break called stop true");
//                    Client c = new Client();
//                    c.setMessage(event.kind().name());
                    stop = true;
                    break;
                }
                if (Client.disconnect) {
                    break;
                }
            }
            if (stop || Client.disconnect) {
                System.out.println("stopped");
                break;
            }
        }
        System.out.println("got there");

    }

    public static void main(String args[]) throws Exception {
        //Path _directotyToWatch = Paths.get("server2");
     
    }
}
