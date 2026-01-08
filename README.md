# Matematika Bersama Garuda

## Description

**Matematika Bersama Garuda** is an AI-powered gamified mobile application designed to improve computational fluency and mental math speed. The application uses a Convolutional Neural Network (CNN) deployed via TensorFlow Lite to recognize handwritten numerical inputs from students in real-time. By automating the grading process, it provides a high-engagement environment for students while reducing the educational burden on parents and teachers.

## Background

Indonesia is currently facing a significant educational crisis in mathematics, with 56% of elementary students failing to meet minimum proficiency standards. Furthermore, approximately 50% of junior high students report disliking mathematics due to uninteresting teaching methods.

To address this, Matematika Bersama Garuda utilizes a handwriting-based interface rather than traditional multiple-choice or typing formats. This approach is supported by neurological research, such as the study "Handwriting but not typewriting leads to widespread brain connectivity" by Van der Weel and Van der Meer, which states: "We urge that children, from an early age, must be exposed to handwriting activities in school to establish the neuronal connectivity patterns that provide the brain with optimal conditions for learning." By integrating AI handwriting recognition, this project targets UN Sustainable Development Goal 4: Quality Education by fostering the neuronal connectivity necessary for effective learning.

## Install

1. Clone the repository to your local machine.
2. Open the project in **Android Studio**.
3. Ensure the `handwritelogic.tflite` model is present in the `app/src/main/assets/` directory.
4. Sync the Gradle files and build the project.
5. Deploy to an Android device or emulator (API 26 or higher recommended).

## Usage

1. Launch the application to reach the **Home Screen**.
2. Select a gameplay mode:
* **Quiz Mode**: A competitive environment to test speed and accuracy.
* **Zen Mode**: A focused practice mode.
3. Solve the presented mathematical problems by drawing the digits (0-9) directly onto the screen's canvas.
4. The AI agent will automatically recognize your handwriting and update the game state based on your answer.

## Maintainers

* **Christopher** (christopher02@student.ciputra.ac.id) 
* **Matthew Regan Hadiwidjaja** (mhadiwidjaja@student.ciputra.ac.id) 
* **Joe Alvaro Pai** (jalvaropai@student.ciputra.ac.id)