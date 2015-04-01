//
//  ViewController.swift
//  PacienteMobile
//
//  Created by Mario Hernandez on 14/03/15.
//  Copyright (c) 2015 Equinox. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    struct MyVariables {
        static var usuario : NSDictionary = ["":""]
    }
    
    @IBOutlet weak var usuarioText: UITextField!
    
    @IBOutlet weak var claveText: UITextField!

    @IBOutlet weak var errorText: UILabel!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
     func logear() -> NSDictionary?{
        
      var  con: Connector = Connector()
        
         con.extraPost("/usuario/autenticar", array: ["email": self.usuarioText.text , "password": self.claveText.text] , verb : "POST")
        
        sleep(2)
        
        return con.result
        
    }

    
    

    func parseJSON(inputData: NSData) -> Array<NSDictionary>{
        var error: NSError?
        var boardsDictionary = NSJSONSerialization.JSONObjectWithData(inputData, options: NSJSONReadingOptions.MutableContainers, error: &error) as Array<NSDictionary>
        
        return boardsDictionary
    }
    

    
    override func shouldPerformSegueWithIdentifier(identifier: String?, sender: AnyObject?) -> Bool {

        var data : NSDictionary? = logear()

        
        
       if (data != nil && data?["rol"]! as NSString == "Paciente"){
        
        MyVariables.usuario = data!
      
            return true
        }else{
        
         self.errorText.hidden = false
           return false
    }
    }
    
  
    
    
}

