# SentimentProject
 Trying to use different approaches of sentiment analysis and put this two sentiment approaches in the analysis page, so users can use both of these two approaches at same time and compare each other.(but still having problems with play2framework form validation)

Detailed Approach:

* Using SparkNLP built in sentiment analysis pipeline
  
* Train a sentiment analysis model using <a href="https://nlp.johnsnowlabs.com/" target="_blank">SparkNLP</a>, data set comes from <a href="https://www.kaggle.com/marklvl/sentiment-labelled-sentences-data-set" target="_blank">https://www.kaggle.com/marklvl/sentiment-labelled-sentences-data-set</a>

* Trying to use <a href="https://github.com/collab-uniba/Emotion_and_Polarity_SO" target="_blank">Emotxt</a> as external java library but keep getting [RuntimeException: java.lang.NoClassDefFoundError:  (also it is too large to upload to github)

* Apche Spark Machine learning library followed by this tutorial(https://databricks.com/wp-content/uploads/2015/10/STEP-3-Sentiment_Analysis.html)
