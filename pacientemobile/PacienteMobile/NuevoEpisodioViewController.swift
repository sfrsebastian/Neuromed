//
//  NuevoEpisodioViewController.swift
//  PacienteMobile
//
//  Created by Mario Hernandez on 15/03/15.
//  Copyright (c) 2015 Equinox. All rights reserved.
//

import UIKit
import AVFoundation



class NuevoEpisodioViewController: UIViewController,
    AVAudioPlayerDelegate, AVAudioRecorderDelegate ,  NSURLSessionDelegate , NSURLSessionTaskDelegate , NSURLSessionDataDelegate
{
    
    @IBOutlet weak var btnGrabar: UIButton!
    
    @IBOutlet weak var btnPlay: UIButton!
    
    @IBOutlet weak var imageView: UIImageView!
    
    var audioRecorder: AVAudioRecorder?
    var audioPlayer: AVAudioPlayer?
    var audioFile : NSData?
    
    var responseData = NSMutableData()
    
    var recorded = false
    
    var localizacion = "Occipital"
    
    var idEpisodio = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @IBAction func crearEpisodio(sender: UIButton) {
        
        var con = Connector()
        
        var idx = ViewController.MyVariables.usuario["id"] as! NSInteger
        
        var todaysDate:NSDate = NSDate()
        var dateFormatter:NSDateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "MM/dd/yyyy"
        var dateInFormat: String = dateFormatter.stringFromDate(todaysDate)
        
        var json : NSDictionary = ["nivelDolor" : ViewController.MyVariables.nivelDolor , "fecha" : dateInFormat , "localizacion" : localizacion , "medicamentos" : ViewController.MyVariables.medicamentos , "causas" : ViewController.MyVariables.causas , "patronesSueno" : ViewController.MyVariables.patrones]
        
        
        
        
        
        
        let resss = con.doPost("/paciente/\(idx)/episodio", dict: json) as NSDictionary
        
        //        print (resss)
        
        idEpisodio = resss["id"] as! Int
        
        if( recorded){
            uploadSound()
            println("Se subió el sonido")
            
            sleep(2)
        }
        
        
        ViewController.MyVariables.creado = true
        
        println("Se acaba de enviar el nuevo episodio y posee el id \(idEpisodio)")
        //                sleep(2)
        
        // Mostrar alerta
        var alert = UIAlertController(title: "Excelente !", message: "Se ha creado el nuevo episodio" , preferredStyle: UIAlertControllerStyle.Alert)
        self.presentViewController(alert, animated: true, completion: nil)
        
        // quitar alerta
        let delay = 0.7 * Double(NSEC_PER_SEC)
        var time = dispatch_time(DISPATCH_TIME_NOW, Int64(delay))
        dispatch_after(time, dispatch_get_main_queue(), {
            alert.dismissViewControllerAnimated(true, completion: {})
            self.popear()
        })

        
        
    }
    
    @IBAction func subirSonido(sender: UIButton) {
        
        var idx = ViewController.MyVariables.usuario["id"] as! NSInteger
        
        var con = Connector()
        
        var readingError:NSError?
        
        var audioFile = NSData(contentsOfURL: audioRecordingPath(),
            options: .MappedRead,
            error: &readingError)
        
        
        if(audioFile != nil){
            
            con.doPutData("/paciente/\(idx)/episodio/\(idEpisodio)", data: audioFile!, params: ["":""], filename: "grabacion.m4a")
        }
    }
    
    func uploadSound(){
        var idx = ViewController.MyVariables.usuario["id"] as! NSInteger
        
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
                
                // registrar que hay grabación
                
                recorded = true
                btnPlay.hidden = false
                //
                
                
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
    
    
    override func touchesEnded(touches: Set<NSObject>, withEvent event: UIEvent)
    {
        let touch = touches.first as! UITouch
        let location = touch.locationInView(self.view)
        
        let x = location.x
        let y = location.y
        
        print("Se ha presionado la locación : \(location)")
        
        if(x >= 102.5 && x <= 261.5 && y <= 152 && y >= 112.5){
            imageView.image = UIImage(named: "frontal")
            localizacion = "Frontal"
        }else if(x >= 88.5 && x <= 183 && y > 152 && y <= 196){
            imageView.image = UIImage(named: "temporal-izquierdo")
            localizacion = "Temporal Izquierdo"
        }else if(x > 183 && x <= 278 && y > 152 && y <= 196){
            imageView.image = UIImage(named: "temporal-derecho")
            localizacion = "Temporal Derecho"
        }else if(x >= 68.5 && x <= 183 && y > 196 && y <= 258.5){
            imageView.image = UIImage(named: "parietal-izquierdo")
            localizacion = "Parietal Izquierdo"
        }else if(x > 183 && x <= 297 && y > 196 && y <= 258.5){
            imageView.image = UIImage(named: "parietal-derecho")
            localizacion = "Parietal Derecho"
        }else if(x >= 85.5 && x <= 183 && y > 258.5 && y <= 307.5){
            imageView.image = UIImage(named: "occipital-izquierdo")
            localizacion = "Occipital Izquierdo"
        }else if(x > 183 && x <= 278 && y > 258.5 && y <= 307.5){
            imageView.image = UIImage(named: "occipital-derecho")
            localizacion = "Occipital Derecho"
        }
    }
    
    func popear (){
//        self.navigationController?.popViewControllerAnimated(true)
//        self.navigationController?.popViewControllerAnimated(true)
        self.navigationController?.popToRootViewControllerAnimated(true)
        
        
    }
}

