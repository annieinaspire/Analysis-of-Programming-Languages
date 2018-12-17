#include <fstream>
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <vector>
#include <regex>

using namespace std;

typedef void (*_load_event_callback) (char*);
typedef void (*_dowork_event_callback) (void);
typedef void (*_end_event_callback) (void);
typedef void (*_word_event_callback) (string);

/* 1 */
class WordFrequencyFramework {
    public:
        WordFrequencyFramework();
        void register_for_load_event(_load_event_callback handler);
        void register_for_dowork_event(_dowork_event_callback handler);
        void register_for_end_event(_end_event_callback handler);
        void run(char* path_to_file);
        
    private:
        int I;
        void (*_load_event_handlers[2]) (char* path_to_file);
        
        int J;
        void (*_dowork_event_handlers[1]) (void);
        
        int K;
        void (*_end_event_handlers[1]) (void);
};

WordFrequencyFramework::WordFrequencyFramework(void) {
    I = 0; J = 0; K = 0;
}

void WordFrequencyFramework::register_for_load_event(_load_event_callback handler) {
    _load_event_handlers[I++] = handler;
}

void WordFrequencyFramework::register_for_dowork_event(_dowork_event_callback handler) {
    _dowork_event_handlers[J++] = handler;
}

void WordFrequencyFramework::register_for_end_event(_end_event_callback handler) {
    _end_event_handlers[K++] = handler;
}

void WordFrequencyFramework::run(char* path_to_file) {
    for(int i = 0; i < I; i++) {
        (*_load_event_handlers[i]) (path_to_file);
    }
    
    for(int j = 0; j < J; j++) {
        (*_dowork_event_handlers[j]) ();
    }
    
    for(int k = 0; k < K; k++) {
        (*_end_event_handlers[k]) ();
    }
}

/* 2 */
class StopWordFilter {
    public:
        static vector<string> _stop_words;
    
        // This is __init__
        StopWordFilter(WordFrequencyFramework *wfapp);
        
        static void __load(char* ignore);
        bool is_stop_word(string word);
};

vector<string> StopWordFilter:: _stop_words;

void StopWordFilter::__load(char* ignore) {
    //cout << ignore << " A" << endl;
    
    ifstream inFile; 
    inFile.open("../stop_words.txt");
    
    string item;
    while (getline(inFile, item, ',')) {
        _stop_words.push_back(item);
    }
    
    _stop_words.push_back("s");
}

bool StopWordFilter::is_stop_word(string word) {
    if (find(_stop_words.begin(), _stop_words.end(), word) != _stop_words.end()) {
        return true;
    }
    else {
        return false;
    }
}

StopWordFilter::StopWordFilter(WordFrequencyFramework *wfapp) {
    wfapp->register_for_load_event(__load);
}

/* 3 */
class DataStorage {
    public:
        static string _data;
        static StopWordFilter *_stop_word_filter;
        
        static int L;
        typedef void (*_word_event_handlers[1]) (string word);
        static _word_event_handlers word_event_handlers;

        // This is __init__
        DataStorage(WordFrequencyFramework *wfapp, StopWordFilter *stop_word_filter);
        
        static void __load(char* path_to_file);
        static void __produce_words(void);
        
        void register_for_word_event(_word_event_callback handler);
};

string DataStorage::_data;
StopWordFilter* DataStorage::_stop_word_filter;

int DataStorage::L;
DataStorage::_word_event_handlers DataStorage::word_event_handlers;

void DataStorage::__load(char* path_to_file) {
    //cout << path_to_file << " B" << endl;
    
    ifstream inFile; 
    inFile.open(path_to_file);
    
    string line;
    while (getline(inFile, line)) {
        _data += line + " ";
    }
    
    regex reg("[\\W]+");
    _data = regex_replace(_data, reg, " ");
    
    for(int i = 0; i < _data.length(); i++) {
        _data[i] = tolower(_data[i]);
    }
    
    //cout << _data << endl;
}

void DataStorage::__produce_words(void) {
    char c_data[_data.size()+1];
    strcpy(c_data,_data.c_str());
    
    char *w;
    w = strtok(c_data, " _");
    while (w != NULL) {
        if (!_stop_word_filter->is_stop_word(w)) {    
            for(int l = 0; l < L; l++) {
                (*word_event_handlers[l]) (w);
            }
        }
        w = strtok(NULL, " _");
    }

    //cout << "C" << endl;
}

void DataStorage::register_for_word_event(_word_event_callback handler) {
    DataStorage::word_event_handlers[L++] = handler;
}

DataStorage::DataStorage(WordFrequencyFramework *wfapp, StopWordFilter *stop_word_filter) {
    L = 0;
    _data = "";
    _stop_word_filter = stop_word_filter;
    wfapp->register_for_load_event(__load);
    wfapp->register_for_dowork_event(__produce_words);
}

/* 4 */
class WordFrequencyCounter {
    public:
        static map<string,int> _word_freqs;
    
        // This is __init__
        WordFrequencyCounter(WordFrequencyFramework *wfapp, DataStorage *data_storage);
        
        static void __increment_count(string word);
        static void __print_freqs(void);
};

map<string,int> WordFrequencyCounter:: _word_freqs;

void WordFrequencyCounter::__increment_count(string word) {
    map<string,int>::iterator it = _word_freqs.find(word);
    
    if(it != _word_freqs.end()) {
        _word_freqs[word] += 1;
    }
    else {
        _word_freqs[word] = 1;
    }
    
    //cout << word << " E" << endl;
}

void WordFrequencyCounter::__print_freqs(void) {
    vector<pair<string, int>> sorted_word_freqs;
    for (auto it = _word_freqs.begin(); it != _word_freqs.end(); ++it) {
        sorted_word_freqs.push_back(*it);
    }
        
    sort(sorted_word_freqs.begin(), sorted_word_freqs.end(), [=](pair<string, int>& a, pair<string, int>& b) {
        return a.second > b.second;
    });
    
    int i = 1;
    vector<pair<string, int>>::iterator it = sorted_word_freqs.begin();
    for (int i = 0; i < 25; i++) {
        cout << it->first << "  -  " << it->second << endl;
        it++;
    }
    
    //cout << "D" << endl;
}

WordFrequencyCounter::WordFrequencyCounter(WordFrequencyFramework *wfapp, DataStorage *data_storage) {
    data_storage->register_for_word_event(__increment_count);
    wfapp->register_for_end_event(__print_freqs);
}

/* 0 */
int main(int argc, char *argv[])
{   
    WordFrequencyFramework wfapp;
    StopWordFilter stop_word_filter(&wfapp);
    DataStorage data_storage(&wfapp, &stop_word_filter);
    WordFrequencyCounter word_freq_counter(&wfapp, &data_storage);
    wfapp.run(argv[1]);
    
    return 0;
}
