package com.obs.brs.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.obs.brs.controller.Score;
import com.obs.brs.model.DataEntry;
import com.obs.brs.model.OcrTextMatchResult;
 

public class DemoServiceBasicUsageFixedDelay
{
	@Autowired
	@ManagedProperty(value ="#{DeService}")
	IDeService deService;
    
	/*@Scheduled(fixedRate = 3600000, initialDelay= 2 *60*1000)*/
    //@Scheduled(fixedRate = 5000)
	@Scheduled(fixedRate = 300000, initialDelay= 2 *60*1000)
    public void demoServiceMethod(){
    	System.out.println("Method executed at every 1 hour. Current time is :: "+ new Date());
    	try {
    		List<DataEntry> croppedJobs = deService.getCropedImagesJobs();
    		//System.out.println("croppedJobs :"+croppedJobs.size());
    		List<DataEntry> liveJobs = deService.getLiveDeData();
    		//System.out.println("Live jobs: "+liveJobs.size());
    		for(DataEntry dc : croppedJobs){
    			//System.out.println("Pid: "+dc.getParentImage().getId() + "OCR: "+dc.getOcrText());
    			if(dc.getOcrText() != null){
    				List<Score> scores = new ArrayList<>();
    				for(DataEntry d : liveJobs){
        				if(d.getOcrText() != null){
        					float relevance = compareSentences(d.getOcrText(),dc.getOcrText());	
            				//System.out.println("Relevance: "+ relevance);	
            				Score s = new Score(d.getId(),relevance); 
            				s.setId(d.getId());
            				s.setScore(relevance);
            				scores.add(s);
        				}
        			}

    				Collections.sort(scores);
    				OcrTextMatchResult ocr = new OcrTextMatchResult();
    				ocr.setCroppedData((dc.getId()));
    				ocr.setLiveData(scores.get(scores.size()-1).getId());
    				ocr.setLiveJobScore(scores.get(scores.size()-1).getScore());
    				ocr.setDuplicate(false);
    				try {
    					
    					if(scores.get(scores.size()-1).getScore() >= .5){
    						deService.saveOcrTextResult(ocr);	
    					}
    					
    					ocr = new OcrTextMatchResult();
    					ocr.setCroppedData((dc.getId()));
    					ocr.setLiveData(scores.get(scores.size()-2).getId());
    					ocr.setLiveJobScore(scores.get(scores.size()-2).getScore());
    					ocr.setDuplicate(false);
    					//System.out.println("scores.size()-1" +(scores.size()-1));
    					if(scores.get(scores.size()-2).getScore() >= .5){
        					deService.saveOcrTextResult(ocr);	
        				}
        					
    				} catch(Exception e){
    					e.printStackTrace();
    				}
    				
    			/*	System.out.println("JoId: "+dc.getId());
    				System.out.println("Score0: "+scores.get(scores.size()-1).getScore() + "Id 1 : " +scores.get(scores.size()-1).getId());
    				System.out.println("Score1: "+scores.get(scores.size()-2).getScore() + " Id 0: " +scores.get(scores.size()-2).getId());
    			 */			
    			}
    		}
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	//System.out.println("Dejobs dealy: ");
    }
	
	

	public float compareSentences(String st0, String st1) {
	 
	  List<String> articles = new ArrayList<String>();
	  articles.add("a");
	  articles.add("the");
	  articles.add("an");
	  articles.add("and");
	  articles.add("or");
	  articles.add("but");
	  articles.add("nor");
	  articles.add("so");
	  articles.add("for");
	  articles.add("yet");
	  articles.add("after");
	  articles.add("although");
	  articles.add("as");
	  articles.add("as if ");
	  articles.add("as long as ");
	  articles.add("because");
	  articles.add("before");
	  articles.add("even if");
	  articles.add("even though");
	  articles.add("if");
	  articles.add("once");
	  articles.add("provided");
	  articles.add("since");
	  articles.add("so that");
	  articles.add("that");
	  articles.add("though");
	  articles.add("till");
	  articles.add("unless");
	  articles.add("until");
	  articles.add("what");
	  articles.add("when");
	  articles.add("whenever");
	  articles.add("wherever");
	  articles.add("whether");
	  articles.add("while");
	  articles.add("accordingly");
	  articles.add("also");
	  articles.add("anyway");
	  articles.add("besides");
	  articles.add("consequently");
	  articles.add("for example");
	  articles.add("finally");
	  articles.add("for instance");
	  articles.add("further");
	  articles.add("furthermore");
	  articles.add("hence");
	  articles.add("however");
	  articles.add("incidentally");
	  articles.add("indeed");
	  articles.add("in fact");
	  articles.add("instead");
	  articles.add("likewise");
	  articles.add("meanwhile");
	  articles.add("moreover");
	  articles.add("namely");
	  articles.add("now");
	  articles.add("of course");
	  articles.add("on the contrary");
	  articles.add("on the other hand ");
	  articles.add("otherwise");
	  articles.add("nevertheless");
	  articles.add("nonetheless");
	  articles.add("similarly");
	  articles.add("so far");
	  articles.add("until now");
	  articles.add("still");
	  articles.add("then");
	  articles.add("therefore");
	  articles.add("thus");
	  
	  int m = 0;
	  float f = 0;
	   
	  // Create a array of words (lowercase) and sort them
	  // in alphabetical order
	  String[] w0 = st0.split(" ");
	  //System.out.println("w0: "+w0.length);
	  for (int i = 0; i < w0.length; i++){
	    w0[i] = w0[i].toLowerCase();
	    List<String> list = new ArrayList<String>(Arrays.asList(w0));
	    if(articles.contains(w0[i])){
	    	list.remove(w0[i]);	
	    }
	    
	    w0 = list.toArray(new String[0]);
	  }
	  Arrays.sort(w0); 
	  //System.out.println("w0: "+w0.length);
	  // Same for the second sentence
	  String[] w1 = st1.split(" ");
	  //System.out.println("w1: "+w1.length);

	  for (int i = 0; i < w1.length; i++){
		  w1[i] = w1[i].toLowerCase();
		  List<String> list = new ArrayList<String>(Arrays.asList(w1));
		  if(articles.contains(w1[i])){
		    list.remove(w1[i]);	
		  }
		  w1 = list.toArray(new String[0]);
	  }
	    
	  Arrays.sort(w1);
	  //System.out.println("w0: "+w1.length);
	  int p0 = 0, p1 = 0;
	  
	  do {
	    int comp = w0[p0].compareTo(w1[p1]);
	    // println(w0[p0] + "  " + w1[p1] + "  " + comp);
	    if (comp == 0) { // same word
	      m++;
	      p0++;
	      p1++;
	    }
	    else if (comp < 0) { // w0 word is before w1 word
	      p0++;
	    }
	    else if (comp > 0) { // w0 word is after w1 word
	      p1++;
	    }
	  }
	  
	  while (p0 < w0.length && p1 < w1.length);
	  f = (2.0f * m) / ( w0.length + w1.length );
	 // System.out.println("Found " + m + " matching words");
	  //System.out.println("Simularity factor: " + f);
	  return f; 
	}
}