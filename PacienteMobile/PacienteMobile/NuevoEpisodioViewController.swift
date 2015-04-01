//
//  NuevoEpisodioViewController.swift
//  PacienteMobile
//
//  Created by Mario Hernandez on 15/03/15.
//  Copyright (c) 2015 Equinox. All rights reserved.
//

import UIKit
import AVFoundation
import Alamofire


class NuevoEpisodioViewController: UIViewController,
AVAudioPlayerDelegate, AVAudioRecorderDelegate ,  NSURLSessionDelegate , NSURLSessionTaskDelegate , NSURLSessionDataDelegate{
    
    @IBOutlet weak var btnGrabar: UIButton!
    
    
    
    var audioRecorder: AVAudioRecorder?
    var audioPlayer: AVAudioPlayer?
    var audioFile : NSData?
    
    var responseData = NSMutableData()
    
    
    
    var idEpisodio = 0
    
    @IBOutlet weak var localizacionText: UITextField!
    @IBOutlet weak var dolorText: UILabel!
    
    @IBOutlet weak var stepper: UIStepper!
    
    @IBAction func stepperAction(sender: UIStepper) {
        
        dolorText.text = Int(sender.value).description
        
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        stepper.wraps = true
        stepper.autorepeat = true
        stepper.maximumValue = 10
        stepper.minimumValue = 1
        
        
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
}
    
    
    @IBAction func crearEpisodio(sender: UIButton) {
        
        var con = Connector()
        
        var idx = ViewController.MyVariables.usuario["id"] as NSInteger
        
        var todaysDate:NSDate = NSDate()
        var dateFormatter:NSDateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "MM/dd/yyyy"
        var dateInFormat: String = dateFormatter.stringFromDate(todaysDate)

        
        var t1 = dolorText.text?.toInt()
        
        var d : [String : AnyObject] =  [ "fecha" : dateInFormat , "localizacion" : localizacionText.text]
        d["nivelDolor"] = t1
        
        con.extraPost("/paciente/\(idx)/episodio", array: d , verb: "POST")
        
        println("Se acaba de enviar el nuevo episodio")
        sleep(2)
        
        
        idEpisodio = con.result?["id"] as NSInteger
        
    //      var readingError:NSError?
        
    //      audioFile = NSData(contentsOfURL: audioRecordingPath(),
    //        options: .MappedRead,
    //        error: &readingError)
        
     //   con.doPutData("/paciente/\(idx)/episodio/\(idEpisodio)", data: audioFile!, params: ["":""], filename: "grabacion.m4a")
  
        
        //println(audioFile)
        
      //  con.postData("/paciente/2/episodio/2", data: audioFile!, vista: self)
      //  con.extraPost("/paciente/2/episodio/2", array: ["grabacion": audioFile] , verb: "PUT")
        
        
        
        // envio del sonido , pero hay que cambiarlo :/
        
        
   //     var connector = Connector()
        
   //     var readingError:NSError?
        
    //    var audioFile = NSData(contentsOfURL: audioRecordingPath(),
     //       options: .MappedRead,
     //       error: &readingError)
        
      //  connector.postData("/url", data: audioFile!, vista: self)
    }
    
    @IBAction func subirSonido(sender: UIButton) {
        
        var idx = ViewController.MyVariables.usuario["id"] as NSInteger
        
             var con = Connector()
        
             var readingError:NSError?
        
        var audioFile = NSData(contentsOfURL: audioRecordingPath(),
               options: .MappedRead,
               error: &readingError)
        

        if(audioFile != nil){

         con.doPutData("/paciente/\(idx)/episodio/\(idEpisodio)", data: audioFile!, params: ["":""], filename: "grabacion.m4a")
        }
    }

    
    
    
    // Github
    
    func audioPlayerBeginInterruption(player: AVAudioPlayer!) {
        /* The audio session is deactivated here */
    }
    
    func audioPlayerEndInterruption(player: AVAudioPlayer!,
        withOptions flags: Int) {
            if flags == AVAudioSessionInterruptionFlags_ShouldResume{
                player.play()
            }
    }
    
    func audioPlayerDidFinishPlaying(player: AVAudioPlayer!,
        successfully flag: Bool){
            
            if flag{
                println("Audio player stopped correctly")
            } else {
                println("Audio player did not stop correctly")
            }
            
            audioPlayer = nil
            
    }
    
    @IBAction func reproducir(sender: UIButton) {
        var readingError:NSError?
        
        audioFile = NSData(contentsOfURL: audioRecordingPath(),
            options: .MappedRead,
            error: &readingError)
        
        var playbackError:NSError?
        
        /* Form an audio player and make it play the recorded data */
        audioPlayer = AVAudioPlayer(data: audioFile, error: &playbackError)
        
        
        
        /* Could we instantiate the audio player? */
        if let player = audioPlayer{
            player.delegate = self
            
            /* Prepare to play and start playing */
            if player.prepareToPlay() && player.play(){
                println("Started playing the recorded audio")
            } else {
                println("Could not play the audio")
            }
            
        } else {
            println("Failed to create an audio player")
        }

        
    }
    

    func audioRecorderDidFinishRecording(recorder: AVAudioRecorder!,
        successfully flag: Bool){
            
            if flag{
                
                println("Successfully stopped the audio recording process")
                
                /* Let's try to retrieve the data for the recorded file */
                
                var readingError:NSError?
                
                    audioFile = NSData(contentsOfURL: audioRecordingPath(),
                    options: .MappedRead,
                    error: &readingError)
                
                var playbackError:NSError?
                
                /* Form an audio player and make it play the recorded data */
                audioPlayer = AVAudioPlayer(data: audioFile, error: &playbackError)
                
                
                
                /* Could we instantiate the audio player? */
               // if let player = audioPlayer{
                 //   player.delegate = self
                    
                    /* Prepare to play and start playing */
                   // if player.prepareToPlay() && player.play(){
                  //      println("Started playing the recorded audio")
                 //   } else {
                //        println("Could not play the audio")
               //     }
                    
              //  } else {
               //     println("Failed to create an audio player")
              //  }
                
            } else {
                println("Stopping the audio recording failed")
            }
            
            /* Here we don't need the audio recorder anymore */
           self.audioRecorder = nil;
            
    }
    
    
    func audioRecordingPath() -> NSURL{
        
        let fileManager = NSFileManager()
        
        let documentsFolderUrl = fileManager.URLForDirectory(.DocumentDirectory,
            inDomain: .UserDomainMask,
            appropriateForURL: nil,
            create: false,
            error: nil)
        
        return documentsFolderUrl!.URLByAppendingPathComponent("grabacion.m4a")
        
    }
    
    func audioRecordingSettings() -> [NSObject : AnyObject]{
        
        /* Let's prepare the audio recorder options in the dictionary.
        Later we will use this dictionary to instantiate an audio
        recorder of type AVAudioRecorder */
        
        return [
            AVFormatIDKey : kAudioFormatMPEG4AAC as NSNumber,
            AVSampleRateKey : 16000.0 as NSNumber,
            AVNumberOfChannelsKey : 1 as NSNumber,
            AVEncoderAudioQualityKey : AVAudioQuality.Low.rawValue as NSNumber
        ]
        
    }
    
    func startRecordingAudio(){
        
        var error: NSError?
        
        let audioRecordingURL = self.audioRecordingPath()
        
        audioRecorder = AVAudioRecorder(URL: audioRecordingURL,
            settings: audioRecordingSettings(),
            error: &error)
        
        if let recorder = audioRecorder{
            
            recorder.delegate = self
            /* Prepare the recorder and then start the recording */
            
            if recorder.prepareToRecord() && recorder.record(){
                
                println("Successfully started to record.")
                
                /* After 5 seconds, let's stop the recording process */
                let delayInSeconds = 5.0
                let delayInNanoSeconds =
                dispatch_time(DISPATCH_TIME_NOW,
                    Int64(delayInSeconds * Double(NSEC_PER_SEC)))
                
                dispatch_after(delayInNanoSeconds, dispatch_get_main_queue(), {
                    [weak self] in
                    self!.audioRecorder!.stop()
                })
                
            } else {
                println("Failed to record.")
                audioRecorder = nil
            }
            
        } else {
            println("Failed to create an instance of the audio recorder")
        }
        
    }
    

    
    
    @IBAction func grabar(sender: UIButton) {
        
        
        /* Ask for permission to see if we can record audio */
        
        var error: NSError?
        let session = AVAudioSession.sharedInstance()
        
        if session.setCategory(AVAudioSessionCategoryPlayAndRecord,
            withOptions: .DuckOthers,
            error: &error){
                
                if session.setActive(true, error: nil){
                    println("Successfully activated the audio session")
                    
                    session.requestRecordPermission{[weak self](allowed: Bool) in
                        
                        if allowed{
                            self!.startRecordingAudio()
                        } else {
                            println("We don't have permission to record audio");
                        }
                        
                    }
                } else {
                    println("Could not activate the audio session")
                }
                
        } else {
            
            if let theError = error{
                println("An error occurred in setting the audio " +
                    "session category. Error = \(theError)")
            }
            
        }
        
    }
    
    
    func URLSession(session: NSURLSession, task: NSURLSessionTask, didCompleteWithError error: NSError?) {
        if error != nil{
            println("Error en la URLSESSION")
        }else{
            println("Upload completed")
        }
        
    }
    
    func URLSession(session: NSURLSession, task: NSURLSessionTask, didSendBodyData bytesSent: Int64, totalBytesSent: Int64, totalBytesExpectedToSend: Int64) {
        var uploadProgress : Double = Double(totalBytesSent)/Double(totalBytesExpectedToSend)
        println("Uploaded \(uploadProgress * 100)%")
    }
    
    func URLSession(session: NSURLSession, dataTask: NSURLSessionDataTask, didReceiveResponse response: NSURLResponse, completionHandler: (NSURLSessionResponseDisposition) -> Void) {
        println("Recieve response \(response)")
        completionHandler(NSURLSessionResponseDisposition.Allow)
    }
    
    func URLSession(session: NSURLSession, dataTask: NSURLSessionDataTask, didReceiveData data: NSData) {
        responseData.appendData(data)
    }


}