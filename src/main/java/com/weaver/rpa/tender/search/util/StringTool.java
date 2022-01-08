package com.weaver.rpa.tender.search.util;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringTool {

    /**
     * 是字母
     * @param str
     * @return
     */
    public static boolean isLetter(String str){
        if(StringUtils.isBlank(str)){
            return false;
        }
        return str.matches("[a-zA-Z]+");
    }

    /**
     * 是数字
     * @param str
     * @return
     */
    public static boolean isDigit(String str){
        if(StringUtils.isBlank(str)){
            return false;
        }
        return str.matches("[0-9]+");
    }

    /**
     * 判断单个字：一个汉字、一个符号、一个英文单词、一个数字
     * @param str
     * @return
     */
    public static boolean isSingleWord(String str){
        if(StringUtils.isBlank(str)){
            return false;
        }
        //单个字、纯英文字母、纯数字
        return str.length()==1 || str.matches("[a-zA-Z]+") || str.matches("\\d+");
    }

    /**
     * 是字母或数字
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str){
        if(StringUtils.isBlank(str)){
            return false;
        }
        return str.matches("[a-zA-Z0-9]+");
    }

    /**
     * 是字母或空格
     * @param str
     * @return
     */
    public static boolean isLetterBlank(String str){
        if(StringUtils.isEmpty(str)){
            return false;
        }
        return str.matches("[a-zA-Z ]+");
    }

    /**
     * 存在字母和数字
     * @param str
     * @return
     */
    public static boolean existsLetterAndDigit(String str){
        if(StringUtils.isBlank(str)){
            return false;
        }
        return str.matches(".*[a-zA-Z]+.*") && str.matches(".*[0-9]+.*");
    }

    /**
     * 是汉字
     * @param str
     * @return
     */
    public static boolean isHanzi(String str){
        if(StringUtils.isBlank(str)){
            return false;
        }
        //中文编码的开始和结束：只能判断汉字，不能判断中文符号
        return str.matches("[\u4E00-\u9FA5]+");
    }

    /**
     * 是否有括号
     * @param text
     * @return
     */
    public static boolean containsBracket(String text){
        if(StringUtils.isBlank(text)){
            return false;
        }
        return text.contains("(") || text.contains(")") || text.contains("（") || text.contains("）") || text.contains("【") || text.contains("】");
    }

    /**
     * 去除括号后面的所有内容
     * @return
     */
    public static String removeBracket(String text){
        if(StringUtils.isBlank(text)){
            return "";
        }
        //去掉人名()里的备注
        int mark = text.indexOf("(");
        if(mark>=0){
            text = StringUtils.substring(text, 0, mark);
        }
        mark = text.indexOf("（");
        if(mark>=0){
            text = StringUtils.substring(text, 0, mark);
        }
        return text;
    }

    /**
     * 去除括号和括号里面的内容
     * @return
     */
    public static String removeBracketAndInternal(String text){
        if(StringUtils.isBlank(text)){
            return "";
        }
        if(!containsBracket(text)){
            return text;
        }
        String[] chs = text.split("");
        boolean correct = true;
        int startBracketNum = 0;//开始括号的数量
        StringBuffer sbf = new StringBuffer();
        for(String ch : chs){
            if(ch.equals("(")||ch.equals("（") || ch.equals("【")){
                correct = false;
                startBracketNum++;
                continue;
            }
            if(ch.equals(")")||ch.equals("）") || ch.equals("】")){
                if(startBracketNum>0){//防止只有结束括号，没有开始括号。导致变成负数
                    startBracketNum--;
                }
                if(startBracketNum<=0){//嵌套的括号都完了
                    correct = true;
                }
                continue;
            }


            if(correct){
                sbf.append(ch);
            }
        }

        String result = replaceBracket(sbf.toString());//去除所有剩余的结束括号
        return result;
    }


    /**
     * 去除手机号码、座机号的括号
     * @return
     */
    public static String removePhoneNumberBracket(String text){
        if(StringUtils.isBlank(text)){
            return "";
        }

        /**
         * 修改前：021-52262600-6468(mobile未及时回复时请打座机)12
         * 修改后：021-52262600-646812
         *
         * 修改前：021-52262600-6577（上班时间请打座机）
         * 修改后：021-52262600-6577
         *
         * 修改前：021-52262600(7250)
         * 修改后：021-522626007250
         *
         * 修改前：工作时间打座机（13501941037）
         * 修改后：13501941037
         *
         * 修改前：021 6061 0967  -  3500（分机号）
         * 修改后：02160610967-3500
         *
         * 修改前：(021)60610964(分机号:6568)
         * 修改后：02160610964
         *
         * 修改前：（021)-20200640-2061
         * 修改后：021-20200640-2061
         */

        String[] splitArray = text.split("");
        int bracketNum = 0;
        StringBuffer sbf = new StringBuffer();
        StringBuffer innerBracketNum = new StringBuffer();//括号内部的数字
        boolean innerBracketContainsUnNum = false;//括号内出现非'数字、-'
        for(String str : splitArray){
            if(StringUtils.isBlank(str)){
                continue;
            }
            if("(".equals(str) || "（".equals(str)){
                bracketNum++;
            }
            if(")".equals(str) || "）".equals(str)){
                bracketNum--;
                if(bracketNum<=0){
                    bracketNum=0;
                    innerBracketContainsUnNum = false;
                }
            }

            Pattern pattern = Pattern.compile("[0-9\\-]+");
            Matcher isNum = pattern.matcher(str);
            if(isNum.matches()){
                if(bracketNum>0){
                    innerBracketNum.append(str);
                    if(innerBracketContainsUnNum){//括号内既有数字、又有非数字。丢弃所有数字
                        innerBracketNum = new StringBuffer();
                    }
                }else{
                    if(innerBracketNum.length()>0){
                        sbf.append(innerBracketNum.toString());
                        innerBracketNum = new StringBuffer();
                    }
                    sbf.append(str);
                }
            }else{
                if(bracketNum>0 && !"(".equals(str) && !"（".equals(str) && !")".equals(str) && !"）".equals(str)){
                    //括号内出现非'数字、-'
                    innerBracketContainsUnNum = true;
                }
                if(innerBracketNum.length()>0 && bracketNum<=0){//括号里全部是'数字、-'才加入
                    sbf.append(innerBracketNum.toString());
                    innerBracketNum = new StringBuffer();
                }
            }
        }

        if(innerBracketNum.length()>0 && bracketNum<=0){//括号里全部是'数字、-'才加入
            sbf.append(innerBracketNum.toString());
        }

        return sbf.toString();
    }

    /**
     * 去除手机号码、座机号的横杠
     * @return
     */
    public static String removePhoneNumberHg(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }

        /**
         * 修改前：021-20200640-2061
         * 修改后：021202006402061
         *
         * 修改前：136-0818-3708
         * 修改后：13608183708
         */
        return text.replaceAll("-","");
    }

    /**
     * 取出属于电话号码的内容
     * @return
     */
    public static List<String> matchPhonePartNum(String str){
        if(StringUtils.isBlank(str)){
            return new ArrayList<>();
        }
        String regex = "[0-9\\-]+";
        return PatternKit.extractStr(str, regex);
    }

    public static String replaceBracket(String text){
        if (StringUtils.isBlank(text)) {
            return "";
        }

        return text.replaceAll("\\(|\\)|（|）|【|】","");
    }


    /**
     * 替换斜线：\
     * @param text
     * @return
     */
    public static String replaceSlash(String text){
        if (StringUtils.isBlank(text)) {
            return "";
        }

        return text.replaceAll("\\\\","");
    }

    /**
     * 替换引号：“
     * @param text
     * @return
     */
    public static String replaceQuote(String text){
        if (StringUtils.isBlank(text)) {
            return "";
        }

        return text.replaceAll("\"","");
    }

    /**
     * 斜线引号：\“    变为   ”
     * @param text
     * @return
     */
    public static String changeSlashQuote(String text){
        if (StringUtils.isBlank(text)) {
            return "";
        }

        return text.replaceAll("\\\"","\"");
    }

    /**
     * 斜线引号：\“:  or  “:  变为   =
     * @param text
     * @return
     */
    public static String changeSlashQuoteToEqual(String text){
        if (StringUtils.isBlank(text)) {
            return "";
        }

        return text.replaceAll("\\\":|\":","=");
    }

    /**
     * 忽略大小写包含字符串
     * @param text 原始字符串
     * @param str 待检查的字符串
     * @return
     */
    public static boolean containsStrIgnoreCase(String text, String str){
        return text.toLowerCase().contains(str.toLowerCase());
    }


    /**
     * 忽略大小写替换字符串
     * @param text 原始字符串
     * @param str 待替换的字符串
     * @param repStr 替换的字符串
     * @return
     */
    public static String replaceStrIgnoreCase(String text, String str, String repStr){
        String pattern = "(?i)"+str;//使用(?i)来忽略字符串大小写
        return text.replaceAll(pattern,repStr);
    }


    /**
     * 中英文数字切分
     * 中文按字切分
     * 英文连续的字母切分在一起
     * 数字连续的数字切分在一起
     * @param content 尾号789 abc
     * @return 尾、号、789、 、abc
     */
    public static List<String> splitChineseEnglishDigit(String content){
        if(null == content || content.isEmpty()){
            return null;
        }
        StringBuilder builder = new StringBuilder();
        char[] chars = content.toCharArray();
        int len = chars.length;
        List<String> charList = new ArrayList<>(len);
        for(int i = 0; i < len; i++){
            // 判断字符串
            while(i < len){
                if(Character.isLowerCase(chars[i]) || Character.isUpperCase(chars[i])){
                    builder.append(chars[i]);
                    i ++;
                } else {

                    break;
                }
            }
            if(builder.length() > 0){
                charList.add(builder.toString());
                builder.setLength(0);
            }
            // 判断数字
            while(i < len){
                if(Character.isDigit(chars[i])){
                    builder.append(chars[i]);
                    i ++;
                } else {
                    break;
                }
            }
            if(builder.length() > 0){
                charList.add(builder.toString());
                builder.setLength(0);
            }
            if(i < len){
                charList.add(String.valueOf(chars[i]));
            }
        }
        return charList;
    }



    /**
     * 获取括号内文本
     * @param text
     * @return
     */
    public static String internalBracket(String text){
        if(StringUtils.isBlank(text)){
            return "";
        }
        List<String> startBracketList = new ArrayList<>();
        startBracketList.add("(");
        startBracketList.add("（");
        List<String> endBracketList = new ArrayList<>();
        endBracketList.add(")");
        endBracketList.add("）");

        for(String sbr : startBracketList){
            int start = text.indexOf(sbr);
            if(start>=0){
                for(String ebr : endBracketList){
                    int end = text.indexOf(ebr,start);
                    if(end>=0){
                        return StringUtils.substring(text, start+1, end);
                    }
                }
            }
        }

        return "";
    }


    /**
     * 生成随机数字字符串
     * @param length 生成字符串的长度
     * @return
     */
    public static String getRandomString(int length) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 汉字转数字
     * @param str
     * @return
     */
    public static int chineseToNumbers(String str){
        if("一".equals(str)){
            return 1;
        }
        if("二".equals(str)){
            return 2;
        }
        if("三".equals(str)){
            return 3;
        }
        if("四".equals(str)){
            return 4;
        }
        if("五".equals(str)){
            return 5;
        }
        if("六".equals(str)){
            return 6;
        }
        if("七".equals(str)){
            return 7;
        }
        if("八".equals(str)){
            return 8;
        }
        if("九".equals(str)){
            return 9;
        }
        if("十".equals(str)){
            return 10;
        }
        return 0;
    }

    /**
     * 过滤出在原始文本中连续(相邻)的词
     * @param text 原始文本
     * @param words 词
     * @return
     */
    public static List<String> filterAdjacentWord(String text, List<String> words){
        if(StringUtils.isBlank(text) || words==null || words.isEmpty()){
            return new ArrayList<>();
        }
        String _text = text.toLowerCase();

        List<String> termArray = Stream.generate(() -> "").limit(words.size()).collect(Collectors.toList());

        /**
         * 排除在句子中不连续匹配的字
         * 语音文本：张波是EBU二部的人吗？
         * 分词：张、波、是、EBU、二、部、的、人、吗
         * 存在候选项的字：、波、、EBU、二、部、、人、
         * 过滤掉不连续的字：、、、EBU、二、部、、、
         */
        String beforeTerm = "";
        int beforeIndex = 0;
        for(int beforePos=0; beforePos<words.size();beforePos++){
            String term = words.get(beforePos);
            if(StringUtils.isBlank(term)){
                beforeTerm = "";
                continue;
            }
            int index = _text.indexOf(term.toLowerCase(), beforeIndex);
            if(index==0){//在字符串的开头
                beforeTerm = term;
                beforeIndex = 0;
                continue;
            }else{
                //第一个开始匹配的字，但在字符串的中间
                //前一个字是不连续的，当前的字作为后面的检查项
                if(beforeIndex==0 && beforePos==0){
                    beforeTerm = term;
                    beforeIndex = index;
                    continue;
                }
            }

            if(beforeIndex+beforeTerm.length()==index){
                termArray.set(beforePos-1, beforeTerm);
                termArray.set(beforePos, term);

                beforeTerm = term;
                beforeIndex = index;
                continue;
            }else{
                beforeTerm = term;
                beforeIndex = index;
                continue;
            }
        }
        return termArray;
    }

    /**
     * 连续的字组成词
     * @param words 空字符串分隔的多个字
     * @return
     */
    public static List<String> joinWords(List<String> words){
        if(words==null || words.isEmpty()){
            return new ArrayList<>();
        }

        List<String> results = new ArrayList<>();
        StringBuffer sbf = new StringBuffer();
        for(String word : words){
            if(StringUtils.isNotBlank(word)){
                sbf.append(word);
            }else{
                if(sbf.length()>0){
                    results.add(sbf.toString());
                    sbf = new StringBuffer();
                }
            }
        }
        if(sbf.length()>0){
            results.add(sbf.toString());
        }
        return results;
    }


    /**
     * 词的方向一致
     * @param text
     * @param match
     * @return
     */
    public static boolean includeAllAndSameDirection(String text, String match){
        if(StringUtils.isBlank(text)){
            return false;
        }
        if(StringUtils.isBlank(match)){
            return true;
        }

        String _text = text.toLowerCase();
        String[] word = match.toLowerCase().split("");
        int beforeIndex=0;
        for(int j=0;j<word.length;j++){
            String w=word[j];
            int index= _text.indexOf(w,beforeIndex);
            if(index<=-1){return false;}
            else{beforeIndex=index;}
        }
        return true;
    }


    /**
     * 去除被其他字符串完全包含的短文本
     * @param list
     * @return
     */
    public static List<String> removeShortStr(List<String> list){
        if(list==null || list.isEmpty()){
            return new ArrayList<>();
        }

        Set<String> copy = list.stream().collect(Collectors.toSet());

        for(String str : list){
            for(String cp : copy){
                if(str.equals(cp)){
                    continue;
                }
                if(cp.contains(str)){
                    copy.remove(str);
                    break;
                }
            }
        }
        return copy.stream().collect(Collectors.toList());
    }


    /**
     * 去除词的所有字，都是其他词的一部分
     * @param list
     * @return
     */
    public static List<String> removePartWord(List<String> list){
        if(list==null){
            return new ArrayList<>();
        }
        if(list.size()<=1){
            return list;
        }

        Set<String> copy = list.stream().collect(Collectors.toSet());
        Set<String> remove = new HashSet<>();

        for(String str : list){
            List<String> wlist = new ArrayList(Arrays.asList(str.split("")));
            for(String cp : copy){
                if(str.equals(cp)){
                    continue;
                }

                for(int i=wlist.size()-1; i>=0; i--){
                    if(cp.contains(wlist.get(i))){
                        wlist.remove(i);
                    }
                }
            }

            if(wlist.isEmpty()){
                remove.add(str);
            }
        }

        copy.removeAll(remove);

        return copy.stream().collect(Collectors.toList());
    }


    /**
     * 移除不包含的字符串
     * @param query
     * @param list
     * @return
     */
    public static List<String> removeUnContainsStr(String query, List<String> list) {
        if(StringUtils.isBlank(query) || list==null || list.isEmpty()){
            return new ArrayList<>();
        }

        return list.stream().filter(s -> containsStrIgnoreCase(query, s)).collect(Collectors.toList());
    }
}
