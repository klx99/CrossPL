cmake_minimum_required(VERSION 3.4.1)

add_library( CrossPLTest )
target_sources( CrossPLTest PRIVATE
  "KotlinTestMethods.cpp"
)
#target_include_directories( CrossPLTest PRIVATE )

target_link_libraries( CrossPLTest log )
