//
//  EpisodioDetail.swift
//  PacienteMobile
//
//  Created by Mario Hernandez on 8/04/15.
//  Copyright (c) 2015 Equinox. All rights reserved.
//

import UIKit

class EpisodioDetail : UIViewController {
    
    @IBOutlet weak var lblNivelDolor: UILabel!
    
    @IBOutlet weak var lblLocalizacion: UILabel!
    
    @IBOutlet weak var btnSonido: UIButton!
    
    var nivelDolor = 0
    
    var localizacion = "No hay"
  
    override func viewDidLoad() {
        super.viewDidLoad()
        lblNivelDolor.text = "\(nivelDolor)"
        lblLocalizacion.text = localizacion
    }
    
    
    
    
    
    
}