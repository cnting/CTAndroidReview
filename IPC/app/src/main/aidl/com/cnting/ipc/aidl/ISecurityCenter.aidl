// ISecurityCenter.aidl
package com.cnting.ipc.aidl;


interface ISecurityCenter {
    String encrypt(String content);

    String decrypt(String password);
}