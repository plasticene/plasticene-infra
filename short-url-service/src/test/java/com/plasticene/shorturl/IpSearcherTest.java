package com.plasticene.shorturl;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/8/16 17:07
 */
import org.lionsoul.ip2region.xdb.Searcher;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class IpSearcherTest {


    public static void main(String[] args) throws IOException {
        searchByFile();
    }

    public static void searchByFile() throws IOException {
        // 1、创建 searcher 对象
        long startTime = System.currentTimeMillis();
        String dbPath = "/Users/shepherdmy/Desktop/ip2region.xdb";
        Searcher searcher = null;
        try {
            searcher = Searcher.newWithFileOnly(dbPath);
        } catch (IOException e) {
            System.out.printf("failed to create searcher with `%s`: %s\n", dbPath, e);
            return;
        }

        // 2、查
        String ip = "10.8.7.177";
        try {
            String region = searcher.search(ip);
            long cost = System.currentTimeMillis()-startTime;
            System.out.printf("{region: %s, ioCount: %d, took: %d ms}\n", region, searcher.getIOCount(), cost);
        } catch (Exception e) {
            System.out.printf("failed to search(%s): %s\n", ip, e);
        }

        // 3、关闭资源
        searcher.close();

        // 备注：并发使用，每个线程需要创建一个独立的 searcher 对象单独使用。
    }

    public static void searchByVectorIndex() throws IOException {
        long startTime = System.currentTimeMillis();
        String dbPath = "/Users/shepherdmy/Desktop/ip2region.xdb";

        // 1、从 dbPath 中预先加载 VectorIndex 缓存，并且把这个得到的数据作为全局变量，后续反复使用。
        byte[] vIndex;
        try {
            vIndex = Searcher.loadVectorIndexFromFile(dbPath);
        } catch (Exception e) {
            System.out.printf("failed to load vector index from `%s`: %s\n", dbPath, e);
            return;
        }

        // 2、使用全局的 vIndex 创建带 VectorIndex 缓存的查询对象。
        Searcher searcher;
        try {
            searcher = Searcher.newWithVectorIndex(dbPath, vIndex);
        } catch (Exception e) {
            System.out.printf("failed to create vectorIndex cached searcher with `%s`: %s\n", dbPath, e);
            return;
        }

        // 3、查询
        String ip = "115.206.246.88";
        try {
            String region = searcher.search(ip);
            long cost = System.currentTimeMillis()-startTime;
            System.out.printf("{region: %s, ioCount: %d, took: %d ms}\n", region, searcher.getIOCount(), cost);
        } catch (Exception e) {
            System.out.printf("failed to search(%s): %s\n", ip, e);
        }

        // 4、关闭资源
        searcher.close();

        // 备注：每个线程需要单独创建一个独立的 Searcher 对象，但是都共享全局的制度 vIndex 缓存
    }

    public static void searchByMemory() {
        long startTime = System.currentTimeMillis();
        String dbPath = "classpath:/ip2region.xdb";

        // 1、从 dbPath 加载整个 xdb 到内存。
        byte[] cBuff;
        try {
            cBuff = Searcher.loadContentFromFile(dbPath);
        } catch (Exception e) {
            System.out.printf("failed to load content from `%s`: %s\n", dbPath, e);
            return;
        }

        // 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。
        Searcher searcher;
        try {
            searcher = Searcher.newWithBuffer(cBuff);
        } catch (Exception e) {
            System.out.printf("failed to create content cached searcher: %s\n", e);
            return;
        }

        // 3、查询
        String ip = "115.206.246.88";
        try {
            long sTime = System.nanoTime();
            String region = searcher.search(ip);
            long cost = System.currentTimeMillis()-startTime;
            System.out.printf("{region: %s, ioCount: %d, took: %d μs}\n", region, searcher.getIOCount(), cost);
        } catch (Exception e) {
            System.out.printf("failed to search(%s): %s\n", ip, e);
        }

        // 4、关闭资源 - 该 searcher 对象可以安全用于并发，等整个服务关闭的时候再关闭 searcher
        // searcher.close();

        // 备注：并发使用，用整个 xdb 数据缓存创建的查询对象可以安全的用于并发，也就是你可以把这个 searcher 对象做成全局对象去跨线程访问。
    }

}
