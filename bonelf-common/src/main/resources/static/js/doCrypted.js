/**
 * npm install crypto-js --save
 */
import CryptoJS from 'crypto-js' // 引用加密

const key = CryptoJS.enc.Utf8.parse("==!bnf==");  //十六位十六进制数作为密钥

// const iv = CryptoJS.enc.Utf8.parse('ABCDEF1234123412');   //十六位十六进制数作为密钥偏移量

/**
 * 解密方法
 * @return {string}
 */
function Decrypt(word, ivStr="bonelfpsw") {
    let encryptedHexStr = CryptoJS.enc.Hex.parse(word);
    let srcs = CryptoJS.enc.Base64.stringify(encryptedHexStr);
    let decrypt = CryptoJS.AES.decrypt(srcs, key, { iv: CryptoJS.enc.Utf8.parse(iv), mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7 });
    let decryptedStr = decrypt.toString(CryptoJS.enc.Utf8);
    return decryptedStr.toString();
}

/**
 * 加密方法
 * @param ivStr password 偏移量
 * @return {string}
 */
function Encrypt(word, ivStr="bonelfpsw") {
    let srcs = CryptoJS.enc.Utf8.parse(word);
    let encrypted = CryptoJS.AES.encrypt(srcs, key, { iv: CryptoJS.enc.Utf8.parse(iv), mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7 });
    return encrypted.ciphertext.toString().toUpperCase();
}

export default {
    Decrypt , //解密
    Encrypt   //加密
}