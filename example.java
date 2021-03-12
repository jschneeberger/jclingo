public class example {
    public static void main(String argv[]) {
        // Attempts to load _clingo.dll (on Windows) or lib_clingo.so (on Linux)
        System.loadLibrary("_clingo");
        // Swig does not generate useful pointer wrappers by default. Instead,
        // one has to configure in the module file, which pointers need
        // wrappers. It is probably also a good idea to have a look at
        // typemaps, because output parameters as used below can be handled
        // more nicely with them.
        intp major = new intp();
        intp minor = new intp();
        intp patch = new intp();
        _clingo.clingo_version(major.cast(), minor.cast(), patch.cast());
        System.out.format("version: %d.%d.%d%n", major.value(), minor.value(), patch.value());
    }
}
