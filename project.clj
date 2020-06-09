(defproject giantbomb "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[cats "0.4.0"]
                 [duct/core "0.8.0"]
                 [duct/module.logging "0.5.0"]
                 [duct/module.web "0.7.0"]
                 [hiccup "1.0.5"]
                 [http-kit "2.3.0"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure.clr/spec.alpha "0.1.176"]]
  :plugins [[duct/lein-duct "0.12.1"]]
  :main ^:skip-aot giantbomb.main
  :cloverage
  {:ns-exclude-regex [#"dev"
                      #"giantbomb.main"
                      #"user"]}
  :resource-paths ["resources" "target/resources"]
  :prep-tasks ["javac" "compile" ["run" ":duct/compiler"]]
  :middleware [lein-duct.plugin/middleware]
  :profiles
  {:dev [:project/dev :profiles/dev]
   :repl {:prep-tasks ^:replace ["javac" "compile"]
          :repl-options {:init-ns user}}
   :uberjar {:aot :all}
   :profiles/dev {}
   :project/dev {:source-paths ["dev/src"]
                 :resource-paths ["dev/resources"]
                 :dependencies [[clj-kondo "2020.05.09"]
                                [eftest "0.5.9"]
                                [hawk "0.2.11"]
                                [http-kit.fake "0.2.2"]
                                [integrant/repl "0.3.1"]
                                [kerodon "0.9.1"]]
                 :plugins [[jonase/eastwood "0.2.5"]
                           [lein-cloverage "1.0.11"]
                           [lein-kibit "0.1.6"]]}}
  :aliases {"clj-kondo" ["run" "-m" "clj-kondo.main"
                         "--lint" "src" "test"]})
