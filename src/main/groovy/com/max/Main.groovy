package com.max

import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import java.nio.file.FileAlreadyExistsException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

static void main(String[] args) {
    println "Hello world!"

    String directory = args[0]
    String sourceDir = "/twitter.com/"
    String jsonTargetDir = "/json/twitter.com/"

    Files.find(Paths.get(directory), Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile()).forEach(path -> {
        File file = path.toFile()
        Document doc = Jsoup.parse(file, "UTF-8", "");
        Elements tweetsContents = doc.select(".js-tweet-text-container p")
        List<String> tweets = new ArrayList<>()
        tweetsContents.forEach(tweet -> tweets.add(tweet.html()));
        JSONObject jsonString = new JSONObject()
        jsonString.put("tweets", tweets)

        String newDir = path.toAbsolutePath().toString().replace(sourceDir, jsonTargetDir)
        newDir = newDir.replace(".html", ".json")
        Path jsonPath = Paths.get(newDir);
        byte[] strToBytes = jsonString.toString().getBytes();

        Files.createDirectories(jsonPath.getParent())
        try {
            Files.createFile(jsonPath)
        } catch (FileAlreadyExistsException ex) {
        }
        Files.write(jsonPath, strToBytes)
    })

}