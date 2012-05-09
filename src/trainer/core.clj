(ns trainer.core
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:use (seesaw core mig font))
  (:load "lang"))

(defmacro dbg[x] `(let [x# ~x] (println "dbg:" '~x "=" x#) x#))

(def MAX-PROB 10)
(def MIN-PROB 0)
(def DATA-BASE "train-data.clj")
(def DATA-BASE-BACKUP "train-data-back.clj")

;; initialized later
(def train-data-ref (ref [{:question "", :answer "", :p-fac 10}]))
(def settings-ref (ref nil))
(def idx-ref (ref 0))
(def sticky-ref (ref false))


(defn rand-index-trainer
  "Return a random index of train-data; the choice is weighted by the
respective :p-fac"
  [train-data]
  (if (pos? (count train-data))
    (let [fac-vec (map :p-fac train-data)  ; gives seq of p-fac e.g.  (3 0 2)
          idx-vec (reduce into [] (map repeat fac-vec (iterate inc 0)))] ; e.g. [0 0 0 2 2]
      (rand-nth idx-vec))
    0))

(defn get-item
  "Return item idx or @idx-ref from @train-data-ref, handle empty train-data-ref"
  ([idx]
     (when idx
       (get @train-data-ref idx)))
  ([]
     (get-item @idx-ref)))

(defn do-backup
  "Make a back-up of the DATA-BASE file"
  []
  (try
    (io/copy (io/file DATA-BASE) (io/file DATA-BASE-BACKUP))
    (catch Exception e nil))) ;; do nothing if file doesn't exist

(defn limit
  "Return x limited to l-bound and u-bound, (l-bound <= u-bound)"
  [l-bound u-bound x]
  (max l-bound (min u-bound x)))

(defn del-idx-in-vec
  "Return a vector containing all but the element with index idx of vector in-vec"
  [idx in-vec]
  (into (vec (take idx in-vec)) (drop (inc idx) in-vec)))

(defn first-num-in-str
  "Return first number in in-string limited to MIN-PROB..MAX-PROB or MAX-PROB if not provided"
  [in-string]
  (let [num-str (re-find #"\d+" in-string)]
    (if (pos? (count num-str))
      (limit MIN-PROB MAX-PROB (Integer/parseInt num-str))
      MAX-PROB)))

(defn change-prob
  "Change the probability to ask this question, e.g. +1 increases"
  [idx val]
  (when-let [is-val (:p-fac (get-item idx))]
    (dosync
     (ref-set sticky-ref true)
     (ref-set train-data-ref (assoc @train-data-ref idx
                                    (assoc (train-data-ref idx) :p-fac
                                           (limit MIN-PROB MAX-PROB (+ is-val val))))))))

(defn make-item-list
  "Return list of questions of all items. Questions are limited to 20 characters"
  []
  (map #(subs % 0 (min 20 (count %)))
       (map :question @train-data-ref)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn lang-lbl
  "Return label in right language, use settings if lang-key not provided"
  ([lbl]
     (lang-lbl lbl (:lang @settings-ref)))
  ([lbl lang-key]
     (let [idx (lang-key lang-idx)
           lbl-vector (lbl lang-struct)
           ret-lbl (get lbl-vector idx)]
       ;; return english version (idx=0) if empty
       (if (or (not ret-lbl) (= "" ret-lbl))
         (get lbl-vector 0)
         ret-lbl))))

(defn is-lang [l-key]
  (= l-key (:lang @settings-ref)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; initialization
(let [slurped (try
                (read-string (slurp DATA-BASE))
                (catch Exception e
                  {:settings {:lang :english},
                   :data [{:question "Exemple for a question: Pi's value?",
                           :answer "3.141592653589793", :p-fac 10}]}))]
  (dosync 
   (ref-set train-data-ref (:data slurped))
   (ref-set settings-ref (:settings slurped))
   (ref-set idx-ref (rand-index-trainer (:data slurped)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; GUI stuff
(native!)
(def Default-F-Width 600)
(def Default-F-Height 300)

(def t-question
  (text
   :multi-line? true
   :editable? false
   ;; initialize:
   :text (print-str (:question (get-item)))))

(def t-answer
  (text
   :multi-line? true
   :editable? true
   :text ""))

(def t-prob-text
  (text
   :editable? false
   :text (:p-fac (get-item))))

(defn default-handler [e]
  (println "Command:" (.getActionCommand e)))

(defn t-next-handler [e]
  (dosync
   (ref-set idx-ref (rand-index-trainer @train-data-ref)))
  (when (pos? (count @train-data-ref))
    (do
      (text! t-answer "")
      (config! t-answer :editable? true)
      (text! t-question (print-str (:question (get-item))))
      (text! t-prob-text (:p-fac (get-item))))))

(defn t-answer-handler [e]
  (config! t-answer :editable? false)
  (text! t-answer
         (print-str (:answer (get-item)))))

(defn t-hint-handler [e]
  (let [full-answer (:answer (get-item))
        n-max (count full-answer)
        n (count (text t-answer))]
    (when (< n n-max)
      (text! t-answer (subs full-answer 0 (inc n))))))

(defn t-save-handler [e]
  (dosync
   (do-backup)
   (spit DATA-BASE {:settings @settings-ref :data @train-data-ref})
   (ref-set sticky-ref false)))

(defn change-prob-handler [e, val]
  (change-prob @idx-ref val)
  (text! t-prob-text
         (:p-fac (get-item))))

(defn -add-edit-item-frame
  [t-item]
  (let [ae-question (text :multi-line? true
                          :editable? true
                          :text (:question t-item))
        ae-answer (text :multi-line? true
                          :editable? true
                          :text (:answer t-item))
        default-p MAX-PROB
        ae-prob-text (text :multi-line? false
                           :editable? true
                           :text (if-let [p-fac (:p-fac t-item)]
                                   p-fac
                                   (str default-p)))
        t-dialog (dialog
                  :title (lang-lbl :trainer-add-edit)
                  :content
                  (mig-panel
                   :constraints ["", "[left]"]
                   :size [Default-F-Width :by Default-F-Height]
                   :items [[(lang-lbl :question) "split, span, gaptop 10, wrap"]
                           [ae-question "span, growx, wmin 500"]
                           [(lang-lbl :answer)   "split, span, gaptop 10, wrap"]
                           [ae-answer   "span, growx, wrap"]
                           [(lang-lbl :prob) "split, gaptop 10, wrap"]
                           [ae-prob-text "span, growx"]])
                  :modal? true
                  :options [(button :text (lang-lbl :delete)
                                    :listen [:action
                                             (fn [e]
                                               (return-from-dialog e :delete))])
                            (button :text (lang-lbl :cancel)
                                    :listen [:action
                                             (fn [e]
                                               (return-from-dialog e nil))])
                            (button :text (lang-lbl :save)
                                    :listen [:action
                                             (fn [e]
                                               (return-from-dialog
                                                e
                                                {:question (text ae-question)
                                                 :answer (text ae-answer)
                                                 :p-fac (first-num-in-str
                                                         (text ae-prob-text))}))])])]
    (-> t-dialog pack! show!)))

(defn add-item-handler [e]
  (when-let [t-item (-add-edit-item-frame nil)]
    (when (not= t-item :delete)
      (dosync
       (ref-set sticky-ref true)
       (ref-set train-data-ref (conj @train-data-ref t-item))
       (ref-set idx-ref (dec (count @train-data-ref))))))
  ;; and use the new item directly to re-check
  (text! t-answer "")
  (config! t-answer :editable? true)
  (text! t-question (print-str (:question (get-item))))
  (text! t-prob-text
         (:p-fac (get-item))))

(defn edit-item-handler [e]
  (when-let [t-item (-add-edit-item-frame (get-item))]
    (if (= t-item :delete)
      (do
        (dosync
         (ref-set sticky-ref true)
         (ref-set train-data-ref (del-idx-in-vec @idx-ref @train-data-ref)))
        ;; and change to new item
        (t-next-handler nil))
      (do
        (dosync
         (ref-set sticky-ref true)
         (ref-set train-data-ref (assoc @train-data-ref @idx-ref t-item)))
        ;; and use the edited item directly to re-check
        (text! t-answer "")
        (config! t-answer :editable? true)
        (text! t-question (print-str (:question (get-item))))
        (text! t-prob-text
               (:p-fac (get-item)))))))

(defn select-handler [e, lb]
  (let [idx (.getSelectedIndex lb)]
    (when-let [t-item (-add-edit-item-frame (train-data-ref idx))]
      (if (= t-item :delete)
        (do
          (dosync
           (ref-set sticky-ref true)
           (ref-set train-data-ref (del-idx-in-vec idx @train-data-ref)))
          (dispose! lb))
        (dosync
         (ref-set sticky-ref true)
         (ref-set train-data-ref (assoc @train-data-ref idx t-item)))))))
           
(defn all-items-handler [e]
  (let [lb (listbox :model (make-item-list))
        f (frame :title (lang-lbl :all-items)
                 :content (scrollable lb)
                 :size [200 :by 300])]
    (listen lb :selection #(select-handler % lb))
    (-> f show!)))
                 
(defn about-handler [e]
  (alert e (lang-lbl :about-text)))

(defn help-handler [e]
  (alert e (lang-lbl :help-text)))

(defn ask-for-saving []
  (-> (dialog
       :content (lang-lbl :ask-saving)
       :option-type :yes-no
       :success-fn t-save-handler)
      pack! show!))

(defn exit-handler [e]
  (when (true? @sticky-ref)
    (ask-for-saving))
  (. System exit 0))

(defn dispose-handler [e]
  (when (true? @sticky-ref)
    (ask-for-saving))
  (dispose! e))
  
(defn -main
  "GUI main function"
  []
  (let
      [lang-bg (button-group)
       t-menu-bar (menubar
                   :items [(menu :text (lang-lbl :file)
                                 :items [(menu-item
                                          :text (lang-lbl :save-database)
                                          :listen [:action t-save-handler])
                                         (menu-item
                                          :text "Dispose (development only)"
                                          :listen [:action dispose-handler])
                                         (menu-item
                                          :text (lang-lbl :exit)
                                          :listen [:action exit-handler])])
                           (menu :text (lang-lbl :item)
                                 :items [(menu-item
                                          :text (lang-lbl :add-item)
                                          :listen [:action add-item-handler])
                                         (menu-item
                                          :text (lang-lbl :edit-item)
                                          :listen [:action edit-item-handler])
                                         (menu-item
                                          :text (lang-lbl :show-all-items)
                                          :listen [:action all-items-handler])
                                         (menu-item
                                          :text (lang-lbl :increase-prob)
                                          :listen [:action
                                                   #(change-prob-handler % 1)])
                                         (menu-item
                                          :text (lang-lbl :decrease-prob)
                                          :listen [:action
                                                   #(change-prob-handler % -1)])])
                           (menu :text (lang-lbl :options)
                                 :items [(menu :text (lang-lbl :language)
                                               :items [(radio :id :english
                                                              :text "english"
                                                              :selected? (is-lang :english)
                                                              :group lang-bg)
                                                       (radio :id :svenska
                                                              :text "svenska"
                                                              :selected? (is-lang :svenska)
                                                              :group lang-bg)
                                                       (radio :id :deutsch
                                                              :text "deutsch"
                                                              :selected? (is-lang :deutsch)
                                                              :group lang-bg)])])
                           (menu :text (lang-lbl :help)
                                 :items [(menu-item
                                          :text (lang-lbl :help)
                                          :listen [:action help-handler])
                                         (menu-item
                                          :text (lang-lbl :about)
                                          :listen [:action about-handler])])])
       t-layout (mig-panel
                 :constraints ["", "[left]"]
                 :size [Default-F-Width :by Default-F-Height]
                 :items [
                         [(label :text "Trainer"
                                :font (font
                                       :style #{:bold :italic}
                                       :size 22))
                          "span, gaptop 10, wrap, center"]
                         [(lang-lbl :question) "split, span, gaptop 10, wrap"]
                         [t-question "span, growx, wmin 500"]
                         [(lang-lbl :answer)   "split, span, gaptop 10, wrap"]
                         [t-answer   "span, growx, wrap"]
                         [(lang-lbl :prob) "split, gaptop 10, wrap"]
                         [t-prob-text "split, span"]
                         ])
       t-s-layout (mig-panel
                   :constraints ["", "[left]"]
                   :items [
                           [(button :text (lang-lbl :hint)
                                    :listen [:action t-hint-handler])]
                           [(button :text (lang-lbl :answer)
                                    :listen [:action t-answer-handler])]
                           [(button :text (lang-lbl :next)
                                    :listen [:action t-next-handler])]])
       t-frame (frame
                :title "Trainer"
                :content (border-panel
                          :north t-menu-bar
                          :center t-layout
                          :south t-s-layout)
                :on-close :exit)]
    ;; lang-button-group listener, seems wrong here, but how to do better?
    (listen lang-bg :selection
            (fn [e]
              (if-let [s (selection lang-bg)]
                ;; why do I need (text s) instead of s only?
                (let [lang-key (keyword (text s))]
                  (dosync
                   (ref-set sticky-ref true)
                   (ref-set settings-ref
                            (assoc @settings-ref :lang lang-key)))
                  (alert e (lang-lbl :restart-required lang-key))))))
    (-> t-frame pack! show!)))

