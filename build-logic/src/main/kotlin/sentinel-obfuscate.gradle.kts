import java.io.File
import java.util.Random

tasks.register("generateObfuscated") {
    val securityBaseDir = File(projectDir, "security/obfuscate")
    val paths = mapOf(
        "android" to "${projectDir}/../ndk/src/main/cpp/sentinel-obfuscate.hpp",
        "ios" to "${projectDir}/../kni/src/nativeInterop/cinterop/obfuscate/sentinel-obfuscate.hpp"
    )

    outputs.upToDateWhen { false }

    doLast {
        val random = Random()
        val maskKey = random.nextInt(254) + 1

        fun xorAndHex(text: String, key: Int): String {
            return text.trim().map { char ->
                val xored = char.code xor key
                "\\x${xored.toString(16).padStart(2, '0')}"
            }.joinToString("")
        }

        paths.forEach { (platform, outputPath) ->
            val configMap = mutableMapOf<String, List<String>>()

            if (platform == "android") {
                configMap["ROOT_PACKAGES"] = listOf("android/root", "packages.sentinel")
                configMap["ROOT_BINARIES"] = listOf("android/root", "binaries.sentinel")
                configMap["ROOT_SU_PATHS"] = listOf("android/root", "su_paths.sentinel")
                configMap["ROOT_SUSPICIOUS_MOUNTS"] = listOf("android/root", "suspicious_mounts.sentinel")
                configMap["HOOK_PACKAGES"] = listOf("android/hook", "packages.sentinel")
            } else {
                configMap["JAILBREAK_DYLIBS"] = listOf("ios/jailbreak", "dylibs.sentinel")
                configMap["JAILBREAK_PLISTS"] = listOf("ios/jailbreak", "plists.sentinel")
                configMap["JAILBREAK_MOUNTS"] = listOf("ios/jailbreak", "mounts.sentinel")
                configMap["JAILBREAK_BINARIES"] = listOf("ios/jailbreak", "binaries.sentinel")
            }

            val generatedArrays = configMap.map { (varName, fileInfo) ->
                val subDir = fileInfo[0]
                val fileName = fileInfo[1]
                val txtFile = File(securityBaseDir, "$subDir/$fileName")

                val linesList = if (txtFile.exists()) {
                    txtFile.readLines().filter { it.isNotBlank() }
                } else {
                    println("WARNING: $platform: ${txtFile.path} not found!")
                    emptyList()
                }

                val hexLines = linesList.joinToString(",\n    ") { "\"${xorAndHex(it, maskKey)}\"" }
                val nullTerminator = if (platform == "android") "" else "NULL\n"

                "static const char* $varName[] = {\n    ${if (hexLines.isEmpty()) "" else hexLines + ","}\n    $nullTerminator};"
            }.joinToString("\n\n")

            val transformFunction = if (platform == "android") {
    """
    #ifdef __cplusplus
    extern "C" {
    #endif
        static std::string transform(const char *str) {
            if (!str) return "";
            std::string result(str);
            for (size_t i = 0; i < result.length(); i++) {
                result[i] ^= MASK_KEY;
            }
            return result;
        }
    
    #ifdef __cplusplus
    }""".trimIndent()
    } else {
    """
    #ifdef __cplusplus
    extern "C" {
    #endif
    
    static inline void transform(char *str) {
        if (!str) return;
        for (int i = 0; str[i] != '\0'; i++) {
            str[i] ^= (char)MASK_KEY;
        }
    }
    
    #ifdef __cplusplus
    }""".trimIndent()
            }

            val fileContent = """
#include ${if (platform == "android") "<string>" else "<unistd.h>"}
#ifndef SENTINEL_OBFUSCATE_HPP
#define SENTINEL_OBFUSCATE_HPP

#define MASK_KEY ${String.format("0x%02X", maskKey)}

$generatedArrays


$transformFunction

#endif
#endif
            """.trim().trimIndent()

            val outputFile = File(outputPath)
            outputFile.parentFile.mkdirs()
            outputFile.writeText(fileContent)
            println("SUCCESS: $platform obfuscation header generated at -> $outputPath")
        }
    }
}