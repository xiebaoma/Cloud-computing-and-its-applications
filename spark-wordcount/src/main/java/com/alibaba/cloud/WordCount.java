package com.alibaba.cloud;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.*;

public class WordCount {
    public static void main(String[] args) {
        // 1. 创建Spark配置与上下文
        SparkConf conf = new SparkConf().setAppName("WordCount").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // 2. 读取文件为RDD
        JavaRDD<String> lines = sc.textFile("word.txt");

        // 3. 停用词集合
        Set<String> stopwords = new HashSet<>(Arrays.asList("the", "and", "of", "to"));

        // 4. 单词处理
        JavaRDD<String> words = lines
                .flatMap(line -> Arrays.asList(line.toLowerCase().split("\\W+")).iterator())
                .filter(word -> word.length() >= 3 && !stopwords.contains(word));

        // 5. 转为(word, 1)键值对
        JavaPairRDD<String, Integer> wordPairs = words.mapToPair(word -> new Tuple2<>(word, 1));

        // 6. 聚合
        JavaPairRDD<String, Integer> wordCounts = wordPairs.reduceByKey(Integer::sum);

        // 7. 按词频降序排序并取前10
        List<Tuple2<String, Integer>> topWords = wordCounts
                .mapToPair(Tuple2::swap)  // (count, word)
                .sortByKey(false)         // 降序
                .mapToPair(Tuple2::swap)  // (word, count)
                .take(10);

        // 8. 打印结果
        for (Tuple2<String, Integer> tuple : topWords) {
            System.out.println("(" + tuple._1 + "," + tuple._2 + ")");
        }

        sc.stop();
    }
}