# Steganography
Steganography is the technique of hiding secret data within an ordinary, non-secret, file or message in order to avoid detection. The secret data is then extracted at its destination. In this project LSB overriding technique is used.


![ezgif-2-4179d5ba63a1](https://user-images.githubusercontent.com/44112210/76521353-aed9df80-648a-11ea-8424-eddb7c9a2509.png)

## How To Run :runner:

:one: open terminal in `src` folder.

:two: compile the program with this command:
``` sh
javac *.java 
```

:three: Run the program with this command:
``` sh
java Program
```
## Formats Supported

### Vessel File(File which will Hide other files)
:one: .png

### Secret File
any file can be hidden using this technique but the `size` of secret file must be smaller than `embedding-capacity` of Vessel file.

---
---


## Overview

![Steganography](https://user-images.githubusercontent.com/44112210/76523210-0463bb80-648e-11ea-8b5d-2e96d84c30cf.png)
___

## Embedding

![Embed](https://user-images.githubusercontent.com/44112210/76523212-062d7f00-648e-11ea-9bef-62d6c5713db2.png)
___

## Extraction

![Extract](https://user-images.githubusercontent.com/44112210/76523227-09c10600-648e-11ea-9aa0-7fb3f5c0e69d.png)
___

## Splitting Byte
![SplitByte and Merge to Pixel](https://user-images.githubusercontent.com/44112210/76523236-0c236000-648e-11ea-95a9-fe94f1c4d296.png)
___
