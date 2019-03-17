cmake_minimum_required(VERSION 3.4.1)

file( GLOB crosspl-extends-SOURCES ".externalNativeBuild/crosspl-extends/*.cpp"
                                   ".externalNativeBuild/crosspl-extends/*.tpp")
file( GLOB crosspl-extends-HEADERS ".externalNativeBuild/crosspl-extends/*.hpp" )

add_library( crosspl )
target_sources( crosspl PRIVATE
                ${crosspl-extends-SOURCES}
                ${crosspl-extends-HEADERS}
                src/main/cpp/utils/Log.cpp
                src/main/cpp/utils/JniUtils.cpp
                src/main/cpp/CrossBase.cpp )
target_include_directories( crosspl PRIVATE )

#file(GLOB Elastos.SDK.Keypair.C-Libraries "${PROJECT_SOURCE_DIR}/build/tmp/Elastos.SDK.Keypair.C/jni/${ANDROID_ABI}/*")
#file(GLOB Elastos.SDK.Keypair.C-HeaderDir "${PROJECT_SOURCE_DIR}/build/tmp/Elastos.SDK.Keypair.C/jni/include")
#target_include_directories( Elastos.SDK.Keypair PRIVATE "${Elastos.SDK.Keypair.C-HeaderDir}" )
#target_link_libraries( Elastos.SDK.Keypair "${Elastos.SDK.Keypair.C-Libraries}" )

find_library( log-lib log )
target_link_libraries( crosspl log )
