#include <jni.h>
#include <string>
#include <tag.h>
#include <tfilestream.h>
#include <fileref.h>


extern "C" JNIEXPORT jstring JNICALL
Java_com_example_kasui_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */
) {

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());

}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_kasui_TagLib_stringFromJNI(JNIEnv *env, jobject thiz, jintArray j_fd) {

    jsize aLength = env->GetArrayLength(j_fd);
    std::vector<jint> trackVector(aLength);
    env->GetIntArrayRegion(j_fd, 0, aLength, trackVector.data());

    std::string allName;

    for (int i = 0; i < trackVector.size(); ++i) {

        int fd = static_cast<int>(trackVector[i]);
        if (fd < 0) {
            continue;
        }

        TagLib::FileStream file(fd, true);
        TagLib::FileRef fileRef(&file);

        if (!fileRef.isNull() && fileRef.tag()) {
            allName += std::string(",") + fileRef.tag()->title().toCString();
        } else {
            //return env->NewStringUTF("FAILED! 2");
        }

    }

    return env->NewStringUTF(allName.c_str());


    return env->NewStringUTF("Working");
}