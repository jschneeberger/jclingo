public class example {

    static class ModelPrinter extends SolveEventCallback {
        @Override
        public boolean call(long type, SWIGTYPE_p_void event, SWIGTYPE_p_bool goon) {
            if (type == _clingoJNI.clingo_solve_event_type_model_get()) {
                SWIGTYPE_p_clingo_model m = _clingo.void_to_model(event);
                size_p n = new size_p();
                _clingo.clingo_model_symbols_size(m, _clingoJNI.clingo_show_type_shown_get(), n.cast());
                clingo_symbol_a symbols = new clingo_symbol_a((int)n.value());
                _clingo.clingo_model_symbols(m, _clingoJNI.clingo_show_type_shown_get(), symbols.cast(), n.value());
                System.out.print("ANSWER:");
                for (int i = 0; i < n.value(); ++i) {
                    size_p l = new size_p();
                    _clingo.clingo_symbol_to_string_size(symbols.getitem(i), l.cast());
                    byte[] bb = new byte[(int)l.value()];
                    _clingo.clingo_symbol_to_string(symbols.getitem(i), bb, l.value());
                    System.out.format(" %s", new String(bb));
                }
                System.out.println();
            }
            return true;
        }
    }

    public static void main(String argv[]) {
        // Attempts to load _clingo.dll (on Windows) or lib_clingo.so (on Linux)
        System.loadLibrary("_clingo");
        // I had a look at what would be the easiest way to handle pointers. It
        // looks like misusing arrays as pointers is the most convenient way to
        // go. The end user of the library should of course never have to write
        // such code!
        // http://swig.org/Doc4.0/Java.html#Java_input_output_parameters
        int_p major = new int_p();
        int_p minor = new int_p();
        int_p patch = new int_p();
        _clingo.clingo_version(major.cast(), minor.cast(), patch.cast());
        System.out.format("version: %d.%d.%d%n", major.value(), minor.value(), patch.value());

        // TODO: the code below completly neglects error handling

        clingo_control_p ctl = new clingo_control_p();
        _clingo.clingo_control_new(null, 0, null, null, 20, ctl.cast());
        // add a program
        _clingo.clingo_control_add(ctl.value(), "base", null, 0, "a. b.");
        // ground it
        clingo_part_t part = new clingo_part_t();
        part.name_set("base");
        part.setParams(null);
        part.setSize(0);
        clingo_part_a parts = new clingo_part_a(1);
        parts.setitem(0, part);
        _clingo.clingo_control_ground(ctl.value(), parts.cast(), 1, null, null);
        // solve it
        clingo_solve_handle_p hnd = new clingo_solve_handle_p();
        ModelPrinter prt = new ModelPrinter();
        _clingo.clingo_control_solve_d(ctl.value(), _clingoJNI.clingo_solve_mode_yield_get(), null, 0, prt, hnd.cast());
        uint_p ret = new uint_p();
        _clingo.clingo_solve_handle_get(hnd.value(), ret.cast());
        _clingo.clingo_solve_handle_close(hnd.value());
        // clean up
        _clingo.clingo_control_free(ctl.value());
    }
}
