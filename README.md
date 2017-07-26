clj-discord
Clojure library for using the Discord API

This is a very minimalist library, but if you just want to build a simple bot that reacts to certain commands and events by sending chat messages, it should be sufficient.

I am using this to run my bot (which is actually the program in the "example" folder). Unless something bad has happened, it should be online now here and should answer the !d100 command.
To use this library:

    clone this repository
    do lein install
    add to your project dependencies [clj-discord "0.1.1-SNAPSHOT"]
    add to your namespace declaration (:require [clj-discord.core :as discord])
    have a look at the code in the "example" folder

