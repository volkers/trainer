;; language data
;; Coding System: utf-8-unix

(def lang-idx {:english 0 :svenska 1 :deutsch 2})
(def lang-struct
  "Holds labels in corresponding language; [english, svenska, deutsch]"
  {:question ["Question" "Fråga" "Frage"]
   :answer ["Answer" "Svar" "Antwort"]
   :prob ["Probability" "Sannolikhet" "Wahrscheinlichkeit"]
   :next ["Next" "Nästa" "Nächste"]
   :hint ["Hint" "Tips" "Hinweis"]
   :file ["File" "Fil" "Datei"]
   :save-database ["Save database" "Spara databas" "Speichere Daten"]
   :exit ["Exit" "Avsluta" "Beenden"]
   :save ["Save" "Spara" "Übernehmen"]
   :cancel ["Cancel" "Avbryt" "Abrechen"]
   :delete ["Delete" "Radera" "Entfernen"]
   :trainer-add-edit ["Trainer Add/Edit" "Trainer Ny/Ändra" "Trainer Neu/Ändern"]
   :item ["Item" "Fråga" "Frage"]
   :add-item ["Add item" "Ny fråga" "Neue Frage"]
   :edit-item ["Edit item" "Ändra frågan" "Frage änderen"]
   :show-all-items ["Show all items" "Visa alla frågor" "Zeige alle Fragen"]
   :increase-prob ["Increase Probability" "Öka sannolikhet" "Erhöhe Wahrscheinlichkeit"]
   :decrease-prob ["Decrease Probability" "Sänka sannolikhet" "Senke Wahrscheinlichkeit"]
   :options ["Options" "Optioner" "Optionen"]
   :language ["Language" "Språk" "Sprache"]
   :help ["Help" "Hjälp" "Hilfe"]
   :help-text ["The main idea of trainer is to give you questions
about things you want to learn in a random manner.

So if you want to learn some new facts do: Item -> Add item and add
your new questions and the respective answers (Leave Probability at 10
for a start) and save them. This builds a question data base.  Now
trainer asks you a random question after pressing the Next buttom. You
can write your answer into the answer field, but don't have to. Check
your answer by clicking Answer.

You get hints by pressing Hint, i.e. the next character of the answer.

When you think you are quite good at a certain question or the
question gets boring and want you to get it more seldom decrease the
probability of this question by klicking Item -> Decrease Probability.
A probability of 0 means you won't get this question again.

You can see all of your question under Item -> Show all items.

It is possible to change the menu language under Options -> Language"

               "Centrala idén för trainer är att ge dig frågor om
saker du önskar lära dig slumpvist.

Om du vill lära dig några fakta, lägg till frågor genom att klicka
Fråga -> Ny fråga och mata in frågorna och svaren. (Låt Sanolikheten
på 10 att börja med.) Spara. Efter några frågor har du en liten
databas och du kan klicka på Nästa och får en slumpvis utvald fråga.
Du kan, men måste inte, skriva ditt svar i svarsfältet. Nu kan du
kontrollera svaret genom att klicka på Svar.

Du får lite tips, dvs. nästa rätta bokstaven om du klickar på Tips.

När frågan blir för lätt eller tråkig kan du får den mera sällan genom
att sänka sannoligheten om du klickar på Fråga -> Sänka
sannolikhet. Om sannoligheten blir 0 betyder det att du får inte igen
denna fråga.

Du ser alla dina frågor under fliken Fråga -> Visa alla frågor.

Det är möjligt att ändra språkinställingen för menyerna under Optioner -> Språk"

               "Die Grundidee des Lernens mit trainer ist, zufällig gewählte Fragen über
Fakten, die Du lernen möchtest, zu stellen

Willst Du Fakten auswendiglernen, füge die entsprechenden Fragen mit
Frage -> Neue Frage und die Antworten hinzu (Lasse die
Wahrscheinlichkeit für's erste auf 10). Damit wird eine Fragensammlung
aufgebaut, aus der Dir trainer nach Klicken auf Nächste eine
zufällige Frage stellt. Du kannst die Antwort im Antwortfeld
eintippen, dies ist aber nicht nötig. Nach Klicken von Antwort kannst
Du Deine Antwort mit der richigen vergleichen.

Durch Klicken von Hinweis bekommst Du den nächsten Buchstaben der
Antwort gegeben.

Wird Dir die Frage zu leicht oder zu langweilg, kannst Du die
Wahrscheinlichkeit, dass die Frage gestellt wird senken: Frage -> Senke
Wahrscheinlichkeit. Eine Wahrscheinlichkeit von 0 bedeutet, dass die
Frage nicht mehr gestellt wird.

Alle Deine Fragen siehst Du unter Frage -> Zeige alle Fragen.

Unter Optionen -> Sprache kann man die Sprache der Menüs ändern."]
   :about ["About" "Om" "Über"]
   :about-text (vec
                (map str ["Trainer is a program helping learn something by heart"
                          "Trainer är ett program som hjälper att lära sig något utantill"
                          "Trainer ist ein Programm, welches beim Auswendiglernen hilft"]
                     (repeat "\n
Copyright (C) 2012 Volker Sarodnick\n\n
Distributed under the Eclipse Public License, the same as Clojure.")))
   :all-items ["All items" "Alla frågor" "Alle Fragen"]
   :ask-saving ["Database changed, save it?" "Databas ändrad, spara den?" "Daten geändert, speichern?"]
   :restart-required ["To get the new language at all labels you
have to restart the program.
Don't forget to save the database."
                      "Programmet måste startas om för att byta
språket på alla ställen.
Glöm inte att spara databasen."
                      "Das Programm muß neu gestartet werden, damit
die Spracheinstellungen überall aktiv werden.
Speichern der Daten nicht vergessen!"]
   })
