cmake_minimum_required(VERSION 3.4.1)

include( "${CROSSPL_CMAKEFILE}" )

add_library( CrossPLTest SHARED )
target_sources( CrossPLTest PRIVATE
        "JavaTestMethods.cpp"
        "KotlinTestMethods.cpp"
        ${CROSSPL_PROXY_SOURCES}
)
target_include_directories( CrossPLTest PRIVATE )

target_link_libraries( CrossPLTest log )
