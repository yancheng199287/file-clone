use walkdir::WalkDir;
use std::fs;
use std::io::{BufWriter, Write};
use std::ffi::{CStr, CString};
use std::os::raw::c_char;
use std::mem;
use std::str;

//对于带有#[no_mangle]属性的函数，rust编译器不会为它进行函数名混淆
#[no_mangle]
pub extern fn recursive_directory(temp_file_path: *const c_char, root_path: *const c_char) -> *const c_char {
    let name = to_string(temp_file_path);
    let root_path = to_string(root_path);

    let temp_file = name;
    let f = fs::File::create(temp_file).expect("Unable to create file");
    let mut buffer_writer = BufWriter::new(f);
    for entry in WalkDir::new(root_path) {
        let entry = entry.unwrap();
        let mut path = entry.path().display().to_string();
        path.push('\n');
        buffer_writer.write_all(path.as_bytes()).expect("Unable to write data to temp file");
    }
    to_ptr("OK".to_string())
}


fn to_string(pointer: *const c_char) -> String {
    let slice = unsafe { CStr::from_ptr(pointer).to_bytes() };
    str::from_utf8(slice).unwrap().to_string()
}

/// Convert a Rust string to a native string
//read more about java call rust, look https://github.com/drrb/java-rust-example
fn to_ptr(string: String) -> *const c_char {
    let cs = CString::new(string.as_bytes()).unwrap();
    let ptr = cs.as_ptr();
    // Tell Rust not to clean up the string while we still have a pointer to it.
    // Otherwise, we'll get a segfault.
    mem::forget(cs);
    ptr
}