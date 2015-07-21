package bi.meteorite.core.api.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CompositeClassLoader extends ClassLoader {

    private final List classLoaders = Collections.synchronizedList(new ArrayList());

    public CompositeClassLoader() {
        add(Object.class.getClassLoader()); // bootstrap loader.
        add(getClass().getClassLoader()); // whichever classloader loaded this jar.
    }

    /**
     * Add a loader to the n
     * @param classLoader
     */
    public void add(ClassLoader classLoader) {
        if (classLoader != null) {
            classLoaders.add(0, classLoader);
        }
    }

    public Class loadClass(String name) throws ClassNotFoundException {
        for (Iterator iterator = classLoaders.iterator(); iterator.hasNext();) {
            ClassLoader classLoader = (ClassLoader) iterator.next();
            try {
                return classLoader.loadClass(name);
            } catch (ClassNotFoundException notFound) {
                // ok.. try another one
            }
        }
        // One last try - the context class loader associated with the current thread. Often used in j2ee servers.
        // Note: The contextClassLoader cannot be added to the classLoaders list up front as the thread that constructs
        // XStream is potentially different to thread that uses it.
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            return contextClassLoader.loadClass(name);
        } else {
            throw new ClassNotFoundException(name);
        }
    }

}