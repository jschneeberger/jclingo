%module (directors="1") _clingo
%include "carrays.i"
%include "cpointer.i"
%include "typemaps.i"

%immutable clingo_location::begin_file;
%immutable clingo_location::end_file;
%immutable clingo_ast_constructor::name;
%immutable clingo_part::name;

%feature("director") SolveEventCallback;
%apply signed char *OUTPUT { char *string };

%include "clingo.h"
%inline %{
#include <clingo.h>
typedef clingo_control_t* _clingo_control_p;
typedef clingo_solve_handle_t* _clingo_solve_handle_p;
class SolveEventCallback {
public:
    virtual bool call(clingo_solve_event_type_t type, void *event, bool *goon) = 0;
};
bool solve_event_callback(clingo_solve_event_type_t type, void *event, void *data, bool *goon) {
    return static_cast<SolveEventCallback*>(data)->call(type, event, goon);
}
bool clingo_control_solve_d(clingo_control_t *control, clingo_solve_mode_bitset_t mode, clingo_literal_t const *assumptions, size_t assumptions_size, SolveEventCallback *cb, clingo_solve_handle_t **handle) {
    return clingo_control_solve(control, mode, assumptions, assumptions_size, solve_event_callback, static_cast<void*>(cb), handle);
}
%}

%pointer_class(int, int_p);
%pointer_class(unsigned int, uint_p);
%pointer_class(size_t, size_p);
%pointer_class(_clingo_control_p, clingo_control_p);
%pointer_class(_clingo_solve_handle_p, clingo_solve_handle_p);
%array_class(clingo_part_t, clingo_part_a);
%array_class(clingo_symbol_t, clingo_symbol_a);
%array_class(char, char_a);
%pointer_cast(void*, clingo_model_t*, void_to_model);
%extend clingo_location {
    bool begin_file_set(char const *value);
    bool end_file_set(char const *value);
}
%extend clingo_part {
    bool name_set(char const *value);
}

%{
#include <clingo.h>

bool clingo_location_begin_file_set(clingo_location_t *loc, char const *value) {
    return clingo_add_string(value, &loc->begin_file);
}

bool clingo_location_end_file_set(clingo_location_t *loc, char const *value) {
    return clingo_add_string(value, &loc->end_file);
}

bool clingo_part_name_set(clingo_part_t *part, char const *value) {
    return clingo_add_string(value, &part->name);
}

%}
