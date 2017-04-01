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
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.util.*;

/**
 * Example to watch a directory (or tree) for changes to files.
 */
public class WatchDirectory {

    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private final boolean recursive;
    private boolean trace = false;
    Path dir;
    WatchEvent<?> event;

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Creates a WatchService and registers the given directory
     */
    WatchDirectory(Path dir, boolean recursive) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
        this.recursive = recursive;

        if (recursive) {
            System.out.format("Scanning %s ...\n", dir);
            registerAll(dir);
            System.out.println("Done.");
        } else {
            register(dir);
        }

        // enable trace after initial registration
        this.trace = true;
    }

    /**
     * Process all events for keys queued to the watcher
     */
    void processEvents() {
        //for (;;) {
        do {
            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                // print out event
                System.out.format("%s: %s\n", event.kind().name(), child);

                //System.err.println(event.kind().name());
                //System.err.println(child);

//                //if deleted break the loop
                if (event.kind().name().equals("ENTRY_DELETE")) {
                    System.out.println("break called stop true");
                    Client c = new Client();
//                    c.setMessage(event.kind().name());
                    break;
                }


                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (recursive && (kind == ENTRY_CREATE)) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        // ignore to keep sample readbale
                    }
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        } while (true);
        System.out.println("out of loop");
    }


    static void usage() {
        System.err.println("usage: java WatchDir [-r] dir");
        System.exit(-1);
    }

//    public void setPath() {
//        try {
//            dir = Paths.get("\\\\kp-26052011\\F\\hemant");
//            boolean recursive = false;
//
//            PathMatcher matcher =
//                    FileSystems.getDefault().getPathMatcher("glob:*.*");
//            System.err.println("" + matcher.toString());
//
//            new WatchDirectory(dir, recursive).processEvents();
//        } catch (Exception ex) {
//            Logger.getLogger(WatchDirectory.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    public static void main(String[] args) throws IOException {
        // parse arguments
//        if (args.length == 0 || args.length > 2) {
//            usage();
//        }
//        boolean recursive = false;
//        int dirArg = 0;
//        if (args[0].equals("-r")) {
//            if (args.length < 2) {
//                usage();
//            }
//            recursive = true;
//            dirArg++;
//        }
        // register directory and process its events
//        Path dir = Paths.get(args[dirArg]);
//        new WatchDir(dir, recursive).processEvents();
//        boolean recursive = false;
//        //String path = System.getProperty("user.home");
//        PathMatcher matcher =
//                FileSystems.getDefault().getPathMatcher("glob:*.*");
//        System.err.println("" + matcher.toString());
//        Path dir = Paths.get("\\\\kp-26052011\\F\\hemant");
//        //Path dir = Paths.get("C:\\Azaz");
//        //Path dir = Paths.get(path);
//        //Path dir = Paths.get(matcher.toString());
//
//        new WatchDirectory(dir, recursive).processEvents();
//        WatchDir w = new WatchDir();
//        w.setPath();
    }
}
