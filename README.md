# KanusukA's Another Sound Interface

Kansui is at surface is an simple music player, which is suited towards Album and as such does not contain many features found on a typical music player application.<br>
Such features include (may include in future): shuffle , tracks view , repeat and more.<br><br>


*NOTE: Features as such will never make it in the application* <br>

*Another NOTE: The code here is very jumbled and messy, I wanted to reach a state where it's able to play music and can be used in my daily life. I don't get much time to work on it
and writing pristine code is not my goal here* <br>

*Another Another NOTE: I like to keep dependencies at a minimum, as such no Dependency Injection and other "helping" libraries have been used. Only add when absolutely required*

## Design

Altho begin a music player application, my primary intent with Kansui is NOT to make a perfect music listening application.<br>
My primary intent with this software is to try a new design system, and experiment with them in implementation.<br><br>

The design theme I have tried to follow here is **boundless**.<br>
This means: <br>
1) Having no "fixed" position of widgets. I want them to flow on Screen. <br>
2) Recycling existing widgets. The best example for it is in the TopBar, which jumps around changing it's text based on the screen and state. <br>
3) Some overlapping of text (not to the point where it's illegible).

## FUTURE

Not many groundbreaking implementations have been made in the application YET. <br>

The two main design implementation I want to incorporate in Kansui are: <br>

1) Text morph : My current work is focused on implementing a shader system that not only renders text but also morphs it seemlessly by character. <br>
2) Shaders : It's not something that's very much of an hassle but I want to implement a shader system that should react to the music being played.  <br>

*I test and experiment shaders in my other project mHorseShoee, so not much is going to happen here in experiment with those unless until it's time to implement them here.*





