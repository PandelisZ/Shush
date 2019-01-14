package me.pandelis.shush.utils

import java.security.GeneralSecurityException
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*


@Throws(GeneralSecurityException::class)
fun loadPrivateKey(key64: String): PrivateKey {
    val clear = base64Decode(key64)
    val keySpec = PKCS8EncodedKeySpec(clear)
    val fact = KeyFactory.getInstance("DSA")
    val priv = fact.generatePrivate(keySpec)
    Arrays.fill(clear, 0.toByte())
    return priv
}


@Throws(GeneralSecurityException::class)
fun loadPublicKey(stored: String): PublicKey {
    val data = base64Decode(stored)
    val spec = X509EncodedKeySpec(data)
    val fact = KeyFactory.getInstance("DSA")
    return fact.generatePublic(spec)
}

fun base64Decode(stored: String): ByteArray? {
    return Base64.getDecoder().decode(stored)
}

@Throws(GeneralSecurityException::class)
fun savePrivateKey(priv: PrivateKey): String {
    val fact = KeyFactory.getInstance("DSA")
    val spec = fact.getKeySpec(
        priv,
        PKCS8EncodedKeySpec::class.java
    )
    val packed = spec.getEncoded()
    val key64 = base64Encode(packed)

    Arrays.fill(packed, 0.toByte())
    return key64
}

fun base64Encode(packed: ByteArray?): String {
    return Base64.getEncoder().encodeToString(packed)
}


@Throws(GeneralSecurityException::class)
fun savePublicKey(publ: PublicKey): String {
    val fact = KeyFactory.getInstance("DSA")
    val spec = fact.getKeySpec(
        publ,
        X509EncodedKeySpec::class.java
    )
    return base64Encode(spec.getEncoded())
}
