(ns twistory.core
  (:require [twistory.ingest :as ingest]
            [twistory.process :as process])
  (:gen-class))

(defmacro dispatch [usage args handlers]
  (let [cmd-args (gensym 'cmd-args)]
    `(let [~cmd-args (flatten ~args)]
       (condp = (first ~cmd-args)
         ~@(apply concat (map #(list (first %) (list (second %) `(rest ~cmd-args))) handlers))
         (apply ~usage ~cmd-args)))))

(defn usage
  [x]
  (println "Usage:")
  (println "\tblocks {ingest, list, help}")
  (println "\ttweets {ingest, list, help}"))

(defn blocks-usage
  [& args]
  (println "Usage:")
  (println "blocks ingest <filename>")
  (println "blocks list"))

(defn blocks
  [& args]
  (dispatch blocks-usage args
            [["ingest" ingest/blocks]
             ["list" process/list-blocks]]))

(defn tweets-usage
  [& args]
  (println "Usage:")
  (println "tweets ingest <filename>")
  (println "tweets list"))

(defn tweets
  [& args]
  (dispatch tweets-usage args
            [["ingest" ingest/tweets]
             ["list" process/list-tweets]]))

(defn -main
  [& args]
  (dispatch usage args
            [["blocks" blocks]
             ["tweets" tweets]]))
