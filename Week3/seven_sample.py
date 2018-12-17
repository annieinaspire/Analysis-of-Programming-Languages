#!/usr/bin/env python
import re, sys, operator

RECURSION_LIMIT = 10000

# count
f0 = lambda word, wordfreqs: wordfreqs[word] + 1 if word in wordfreqs else 1
f1 = lambda word, stopwords, wordfreqs: f0(word, wordfreqs) if word not in stopwords else 0
f2 = lambda word, stopwords, wordfreqs: f1(word, stopwords, wordfreqs)
count = lambda word, stopwords, wordfreqs: f2(word, stopwords, wordfreqs) if words != [] else 0
       
# wf_print
f3 = lambda wordfreq: sorted(wordfreq.iteritems(), key=operator.itemgetter(1), reverse=True)[:25] if wordfreq != None else None
wf_print = lambda wordfreq: map(p, wordfreq)

# print funciton used by wf_print
def p(wordfreq):
    for i in range(0, len(wordfreq), 2):
        print(str(wordfreq[i]) + "  -  " + str(wordfreq[i + 1]))
    return

# main
stop_words = set(open('../stop_words.txt').read().split(',')) 
words = re.findall('[a-z]{2,}', open(sys.argv[1]).read().lower()) 
word_freqs = {}

# count
for i in range(0, len(words), RECURSION_LIMIT):
    words_list = words[i:i+RECURSION_LIMIT]
    for j in range(0, len(words_list)):
        word_freqs[words_list[j]] = count(words_list[j], stop_words, word_freqs)

# wf_print
wf_print(f3(word_freqs))
