package uk.ac.rothamsted.ide.text.extractor;

/**
 * Code taken from on ca.unbsj.cbakerlab.pdfminer.PDFMiner.java
 * This class reads a file with non ASCII Characters in it.
 * Replaces the non ASCII Characters.
 */

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class EncodingCleaner {
    private static final Logger logger = LogManager.getLogger(EncodingCleaner.class);
    static Map<Integer, String> characterMap = new HashMap() {
        {
            put(64257, "fi");
            put(64258, "fl");
            put(945, "alpha ");
            put(946, "beta ");
            put(947, "gamma ");
            put(956, "mu ");
            put(8220, "\"");
            put(8221, "\"");
            put(8217, " ");
            put(8211, "-");
            put(8224," ");
            put(969, "o");
            put(181, " ");
            put(252, "u");
            put(228, "a");
            put(177, "+/-");
            put(176, "o");
            put(174, "(R)");
            put(21, ">");
            put(24, "~");
            put(12, " ");
            put(169, " ");
            put(1, "x");
            put(3, "\"");
            put(2, "\"");
            put(4, "\"");
            put(1001," ");
            put(1002," ");
            put(1003," ");
            put(1004," ");
            put(1005," ");
            put(1006," ");
            put(1009," ");
            put(1011," ");
            put(1015," ");
            put(1021," ");
            put(1022," ");
            put(1032," ");
            put(1034," ");
            put(1080," ");
            put(1091," ");
            put(133,"...");
            put(134," ");
            put(1349," ");
            put(135," ");
            put(1350," ");
            put(1541," ");
            put(1545," ");
            put(1546," ");
            put(1549," ");
            put(1550," ");
            put(1554," ");
            put(160," ");
            put(1601," ");
            put(161," ");
            put(162," ");
            put(163," ");
            put(165," ");
            put(167," ");
            put(168," ");
            put(170," ");
            put(171," ");
            put(172," ");
            put(175," ");
            put(180," ");
            put(182," ");
            put(183," ");
            put(184," ");
            put(185," ");
            put(186," ");
            put(187," ");
            put(188," ");
            put(192," ");
            put(193," ");
            put(194," ");
            put(195," ");
            put(197," ");
            put(198," ");
            put(200," ");
            put(211," ");
            put(213," ");
            put(215," ");
            put(222," ");
            put(223," ");
            put(224," ");
            put(225," ");
            put(226," ");
            put(229," ");
            put(230," ");
            put(233," ");
            put(235," ");
            put(239," ");
            put(240," ");
            put(241," ");
            put(242," ");
            put(246," ");
            put(247," ");
            put(248," ");
            put(254," ");
            put(255," ");
            put(2845," ");
            put(3041," ");
            put(305," ");
            put(322," ");
            put(338," ");
            put(339," ");
            put(352," ");
            put(353," ");
            put(381," ");
            put(5008," ");
            put(63193," ");
            put(63721," ");
            put(64256,"f");
            put(64259,"fi");
            put(648," ");
            put(7009," ");
            put(7015," ");
            put(7033," ");
            put(7039," ");
            put(7050," ");
            put(710," ");
            put(711," ");
            put(728," ");
            put(730," ");
            put(732," ");
            put(8212,"-");
            put(8216,"\'");
            put(8218," ");
            put(8222," ");
            put(8225," ");
            put(8226," ");
            put(8230," ");
            put(8235," ");
            put(8236," ");
            put(8240," ");
            put(8242," ");
            put(8260," ");
            put(8270," ");
            put(8482," ");
            put(8486," ");
            put(849," ");
            put(850," ");
            put(851," ");
            put(852," ");
            put(8528," ");
            put(858," ");
            put(8592," ");
            put(8593," ");
            put(8594," ");
            put(8595," ");
            put(8596," ");
            put(862," ");
            put(8709," ");
            put(8710," ");
            put(8722," ");
            put(8727," ");
            put(873," ");
            put(8730," ");
            put(874," ");
            put(875," ");
            put(876," ");
            put(8764," ");
            put(8776," ");
            put(8801," ");
            put(8804," ");
            put(8805," ");
            put(881," ");
            put(8834," ");
            put(888," ");
            put(9004," ");
            put(9024," ");
            put(916," ");
            put(9251," ");
            put(9252," ");
            put(9253," ");
            put(9258," ");
            put(9260," ");
            put(9261," ");
            put(9262," ");
            put(9266," ");
            put(9268," ");
            put(9270," ");
            put(9273," ");
            put(9275," ");
            put(948," ");
            put(955," ");
            put(957," ");
            put(963," ");
            put(9632," ");
            put(964," ");
            put(9642," ");
            put(967," ");
            put(9702," ");
            put(9993," ");

        }
    };




    private static String replaceCharAt(String s, int pos, char c) {
        StringBuffer buf = new StringBuffer( s );
        buf.setCharAt( pos, c );
        return buf.toString( );
    }

    private static String removeCharAt(String s, int pos) {
        StringBuffer buf = new StringBuffer( s.length() - 1 );
        logger.debug("-1 "+s);
        logger.debug("-2 "+s.length());
        logger.debug("-3 "+pos);
        buf.append(
                        s.substring(0,pos) )
                .append(
                        s.substring(pos+1) );
        return buf.toString();
    }
    private static String replaceCharWithStringAt(String s, int pos, String c) {
        StringBuffer buf = new StringBuffer( s.length() - 1 );
        // int length =
        // System.out.println("@@@: "+s+"\n - "+s.substring(0,pos) + "\n -- " + s.substring(pos+c.length()));
        buf.append( s.substring(0,pos) ).append(c).append( s.substring(pos+1) );
        return buf.toString();
    }

    static String cleanEncoding(String text){
        try {

            // Below is a workaround for a problem with XML-1.0
            // (that was fixed in XML-1.1).
            // http://tech.groups.yahoo.com/group/jena-dev/message/34417

            text = text.replace((char) 0, ' ');
            text = text.replace((char) 1, ' ');
            text = text.replace((char) 2, ' ');
            text = text.replace((char) 3, ' ');
            text = text.replace((char) 4, ' ');
            text = text.replace((char) 5, ' ');
            text = text.replace((char) 6, ' ');
            text = text.replace((char) 7, ' ');
            text = text.replace((char) 8, ' ');
            text = text.replace((char) 9, ' ');
            text = text.replace((char) 10, ' ');
            text = text.replace((char) 11, ' ');
            text = text.replace((char) 12, ' ');
            text = text.replace((char) 13, ' ');
            text = text.replace((char) 14, ' ');
            text = text.replace((char) 15, ' ');
            text = text.replace((char) 16, ' ');
            text = text.replace((char) 17, ' ');
            text = text.replace((char) 18, ' ');
            text = text.replace((char) 19, ' ');
            text = text.replace((char) 20, ' ');
            text = text.replace((char) 21, ' ');
            text = text.replace((char) 22, ' ');
            text = text.replace((char) 23, ' ');
            text = text.replace((char) 24, ' ');
            text = text.replace((char) 25, ' ');
            text = text.replace((char) 26, ' ');
            text = text.replace((char) 27, ' ');
            text = text.replace((char) 28, ' ');
            text = text.replace((char) 29, ' ');
            text = text.replace((char) 30, ' ');
            text = text.replace((char) 31, ' ');

            // all characters from the extended ASCII table
            for (char c = 128; c <= 255; ++c)
                text = text.replace(c, ' ');

            int count = 0;
            List<String> virtualFile = Arrays.asList(text.split("\n"));

            ListIterator<String> iter = virtualFile.listIterator();

            while (iter.hasNext()) {
                String line = iter.next();
                int sz = line.length();
                logger.debug("sz: "+sz);
                for (int i = 0; i < sz; i++) {

                    if((int)line.charAt(i)<32){
                        logger.debug("char: "+line.charAt(i)+" "+(int)line.charAt(i));
                    }
                    //** For debugging purpose is very GOOD!!!
                    if((int)line.charAt(i)>127 && !characterMap.containsKey((int)line.charAt(i))){
                        logger.debug("put("+(int)line.charAt(i)+","+line.charAt(i)+");");

                        //log.debug(line.charAt(i)+": "+(int)line.charAt(i));
                        //log.debug("line: "+line);
                        //log.debug("line: "+line.charAt(0));
                    }

                    //|| (int)line.charAt(i)==12

                    if ((int)line.charAt(i)==8594) {
                        line = replaceCharAt(line,i,'-');
                        line = replaceCharAt(line,i+1,'>');
                        if((i-1)>=0){
                            line = removeCharAt(line,i-1);
                        }else{
                            line = removeCharAt(line,i);
                        }
                        sz--;
                        i--;
                    }else if(characterMap.containsKey((int)line.charAt(i))){
                        line = replaceCharWithStringAt(line,i,characterMap.get((int)line.charAt(i)));
                    }else if ((int)line.charAt(i)==8211) {
                        // if at the start or at the end
                        if(i==0 || i==sz-1){
                            line = removeCharAt(line,i);
                            sz--;
                            i--;
                            continue;
                        }

                        // if in the middle of the string
                        logger.debug("i: "+i);
                        logger.debug((int)line.charAt(i-1)+"+"+(int)line.charAt(i+1));
                        if(((int)line.charAt(i-1)>47 && (int)line.charAt(i-1)<58)
                                && (47<(int)line.charAt(i+1) && (int)line.charAt(i+1)<58)
                        ){logger.debug("@0 "+line.charAt(i-1)+" "+line.charAt(i+1));
                            line = removeCharAt(line,i);
                            sz--;
                            i--;
                        }else if(( ((int)line.charAt(i-1)>=97 && (int)line.charAt(i-1)<=122) || (int)line.charAt(i-1)==32)
                                && ( ((int)line.charAt(i+1)>=97 && (int)line.charAt(i+1)<=122) || (int)line.charAt(i+1)==32)
                        ){
                            logger.debug("@1 "+(int)line.charAt(i-1)+" "+(int)line.charAt(i+1));
                            logger.debug(line);
                            line = replaceCharAt(line,i,' ');
                        }else{
                            logger.debug("@2");
                            logger.debug(line);
                            line = replaceCharAt(line,i,'-');
                        }
                        logger.debug(line.charAt(i)+": "+(int)line.charAt(i)+" ----> "+line);
                    }

                }
                //line = line.replaceAll("-", " ");



                line = line.trim();

                // if (count == 0)
                //line = line.trim().replaceAll("", " ");
                //else
                //line = ("\n" + line.trim().replaceAll("  ", " "));

                iter.set(line);
                if(iter.hasNext())iter.add("\n");
                count++;
            }

            //System.out.println("virtualFile: "+virtualFile);
            return Arrays.toString(virtualFile.toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;

    }


	/*
	private static void restoreText(String text) {
		try {
			List<String> virtualFile = Arrays.asList(text.split("\n"));

			ListIterator<String> iter = virtualFile.listIterator();

            while (iter.hasNext()) {
            	String line = iter.next();
            	//line = line.replaceAll("\\/", " / ");

            	// restore '->' in mutations
            	line = line.replaceAll(" -3 ", " ");
            	line = line.replaceAll(" -- ", "  ");
            	line = line.replaceAll(" -\\+ ", "  ");
            	line = line.replaceAll("-\\* ", "  ");
            	line = line.replaceAll("-\\)", "  ");

            	line = line.replaceAll("  ", " ");


            	if(line.endsWith(" ")){
            		iter.set(line);
            	} else if(line.endsWith("-")){
            		line = line.substring(0, line.length()-1);
            		iter.set(line);
            	}else if(line.startsWith("Figure")){
            		iter.set(line);
            	}else{
            		iter.set(line);
            		// try with uncommented !!!!!!
                  //  if(iter.hasNext())iter.add("\n");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

	}



	private static void removeIrrelevant(String text)throws IOException, ClassNotFoundException {
		List<String> virtualFile = Arrays.asList(text.split("\n"));

		ListIterator<String> iter = virtualFile.listIterator();

		boolean removeRest = false;

        while (iter.hasNext()) {
        	String line = iter.next();

        	if(removeRest){
        		iter.remove();
        		continue;
        	}

        	//System.out.println("\nline:  "+line);
        	line = line.replaceAll("^[ ]+", "");

			if(line.startsWith("Figure")
					|| line.startsWith("FIGURE")
					|| line.startsWith("Fig.")
					|| line.startsWith("FIG.")
					|| line.startsWith("Table")
					|| line.startsWith("TABLE")
					|| line.startsWith("Tab.")
					|| line.startsWith("TAB.")){
					//log.info("(Fig) line to remove:  "+line);
				iter.remove();
				continue;
			}

			//if(line.contains("Abbreviation") || line.contains("abbreviation")){
				//System.out.println("(Abr) line to remove:  "+line);
				//iter.remove();
				//continue;
			//}


			//if(line.contains("©")){
			//	System.out.println("(c) line to remove:  "+line);
			//	iter.remove();
			//	continue;
			//}



			if(line.startsWith("Acknowledgement")
					|| line.startsWith("acknowledgement")
					|| line.startsWith("ACKNOWLEDGEMENT")
					|| line.startsWith("Reference")
					|| line.startsWith("REFERENCE")
					|| line.startsWith("reference")
					|| line.startsWith("Award")
					|| line.startsWith("award")
					|| line.startsWith("AWARD")){
					//log.info("(Ack) line to remove:  "+line);
				iter.remove();
				if(iter.nextIndex() > (virtualFile.size()/2)){
					removeRest = true;
				} else {
					System.out.println("WARNING: reference section is wrong recognized in file");
				}
			}

		}
	}
	*/


}