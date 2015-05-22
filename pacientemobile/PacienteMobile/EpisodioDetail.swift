//
//  EpisodioDetail.swift
//  PacienteMobile
//
//  Created by Mario Hernandez on 8/04/15.
//  Copyright (c) 2015 Equinox. All rights reserved.
//

import UIKit
import AVFoundation

class EpisodioDetail : UIViewController , UITableViewDelegate,UITableViewDataSource {
    
    @IBOutlet weak var lblNivelDolor: UILabel!
    
    @IBOutlet weak var lblLocalizacion: UILabel!
    
    @IBOutlet weak var lblFecha: UILabel!
    
    @IBOutlet weak var btnSonido: UIButton!
    
    @IBOutlet weak var imageView: UIImageView!
    
    @IBOutlet weak var tableView: UITableView!
    
    
    @IBOutlet weak var segmentedControl: UISegmentedControl!
    
    
    var nivelDolor = 0
    
    var localizacion = "No hay"
    
    var fecha = "jumm"
    
    var episodio = NSDictionary()
    
    var player = AVPlayer()
    
//    var tableData = NSArray()
  
    override func viewDidLoad() {
        super.viewDidLoad()
       var n = episodio["nivelDolor"] as! Int
        lblNivelDolor.text = "\(n)"
        lblLocalizacion.text = episodio["localizacion"] as? String
        lblFecha.text = episodio["fecha"] as? String
        
        tableView.dataSource = self
        tableView.delegate = self
        
        print (episodio)
        
        
        if(episodio["grabacion"]!.isKindOfClass(NSNull)){
            btnSonido.hidden = true
        }
        
        
        
        
        if(lblLocalizacion.text == "Frontal"){
            imageView.image = UIImage(named: "frontal")
        }else if(lblLocalizacion.text == "Temporal Izquierdo"){
            imageView.image = UIImage(named: "temporal-izquierdo")
        }else if(lblLocalizacion.text == "Temporal Derecho"){
            imageView.image = UIImage(named: "temporal-derecho")
        }else if(lblLocalizacion.text == "Parietal Izquierdo"){
            imageView.image = UIImage(named: "parietal-izquierdo")
        }else if(lblLocalizacion.text == "Parietal Derecho"){
            imageView.image = UIImage(named: "parietal-derecho")
        }else if(lblLocalizacion.text == "Occipital Izquierdo"){
            imageView.image = UIImage(named: "occipital-izquierdo")
        }else if(lblLocalizacion.text == "Occipital Derecho"){
            imageView.image = UIImage(named: "occipital-derecho")
        }
        
        
    }
    
    @IBAction func seleccionar(sender: UISegmentedControl) {
        tableView.reloadData()
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        var num = 0
        
        if(segmentedControl.selectedSegmentIndex == 0){
            num = episodio["medicamentos"]!.count
        }else if(segmentedControl.selectedSegmentIndex == 1){
            num = episodio["causas"]!.count
        }else{
            num = episodio["patronesDeSueno"]!.count
        }
        
        return num;
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell  = UITableViewCell()
        
        var x : String = ""
        
        if(segmentedControl.selectedSegmentIndex == 0){
            x = (episodio["medicamentos"] as! NSArray)[indexPath.row]["titulo"] as! String
        }else if(segmentedControl.selectedSegmentIndex == 1){
            x = (episodio["causas"] as! NSArray)[indexPath.row]["titulo"] as! String
        }else{
            x = (episodio["patronesDeSueno"] as! NSArray)[indexPath.row]["titulo"] as! String
        }
        
        
   
        cell.textLabel?.text = x
        
        return cell
        
    }
    
    
    @IBAction func reproducirSonido() {
        
        let url = episodio["grabacion"] as! String
        let playerItem = AVPlayerItem( URL:NSURL( string:url ) )
        player = AVPlayer(playerItem:playerItem)
        player.rate = 1.0;
        player.play()
        
    }
    
    
}