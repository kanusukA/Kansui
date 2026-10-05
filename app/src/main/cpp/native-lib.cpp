#include <jni.h>
#include <string>
#include "stdio.h"
#include "iomanip"
#include "cstdio"
#include "iostream"
#include "tpropertymap.h"
#include "tstringlist.h"
#include "tvariant.h"
#include <tag.h>
#include <tfilestream.h>
#include <fileref.h>
#include "flacfile.h"
#include "xiphcomment.h"
#include "map"


extern "C" JNIEXPORT jstring JNICALL
Java_com_example_kasui_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */
) {

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());

}
extern "C"
JNIEXPORT jobject JNICALL
Java_com_example_kasui_TagLib_stringFromJNI(JNIEnv *env, jobject thiz, jintArray j_fd,
                                            jlongArray jIdArray) {

    jsize aLength = env->GetArrayLength(j_fd);
    std::vector<jint> trackVector(aLength);
    env->GetIntArrayRegion(j_fd, 0, aLength, trackVector.data());

    jsize idLength = env->GetArrayLength(jIdArray);
    std::vector<jlong> idVec(idLength);
    env->GetLongArrayRegion(jIdArray, 0, idLength, idVec.data());

    if (trackVector.size() != idVec.size()) {
        throw std::runtime_error("INVALID IDs PROVIDED");
    }

    std::string output;


    // 1. Find classes and methods
    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jmethodID arrayListInit = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAdd = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

    jclass hashMapClass = env->FindClass("java/util/HashMap");
    jmethodID hashMapInit = env->GetMethodID(hashMapClass, "<init>", "()V");
    jmethodID hashMapPut = env->GetMethodID(hashMapClass, "put",
                                            "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");

    // 2. Instantiate the ArrayList
    jobject arrayListObj = env->NewObject(arrayListClass, arrayListInit);
    int index = 0;

    jstring key;
    jstring data;
    jobject hashMapObj;
    // std::vector<std::map<std::string, std::string>> map{};

    for (int i: trackVector) {

        int fd = static_cast<int>(i);
        if (fd < 0) {
            continue;
        }

        TagLib::FileStream file(fd, true);
        TagLib::FLAC::File fileRef(&file, false);

        if (fileRef.isValid()) {
            hashMapObj = env->NewObject(hashMapClass, hashMapInit);

            key = env->NewStringUTF("id");
            data = env->NewStringUTF(std::to_string(idVec[index]).c_str());

            //  map.push_back({{"id", std::to_string(idVec[index])}});

            env->CallObjectMethod(hashMapObj, hashMapPut, key, data);

            for (auto &field: fileRef.xiphComment()->fieldListMap()) {

                TagLib::String id_str = field.first;
                TagLib::String val_str;
                for (auto &sec: field.second) {
                    val_str += sec.to8Bit();
                }


                key = env->NewStringUTF(id_str.toCString(true));
                data = env->NewStringUTF(val_str.toCString(true));
                env->CallObjectMethod(hashMapObj, hashMapPut, key, data);

            }

            env->CallBooleanMethod(arrayListObj, arrayListAdd, hashMapObj);

            env->DeleteLocalRef(hashMapObj);
            env->DeleteLocalRef(key);
            env->DeleteLocalRef(data);

        } else {
            continue;
        }
        index += 1;


    }


    env->DeleteLocalRef(arrayListClass);
    env->DeleteLocalRef(hashMapClass);


    return arrayListObj;
}