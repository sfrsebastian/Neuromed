//
//  PacienteViewController.swift
//  DoctorApp
//
//  Created by Felipe Macbook Pro on 5/6/15.
//  Copyright (c) 2015 Felipe-Otalora. All rights reserved.
//

import UIKit

class PacienteViewController: UIViewController, LineChartDelegate {
    
    @IBOutlet weak var lblNombre : UILabel?
    @IBOutlet weak var lblFecha : UILabel?
    @IBOutlet weak var lblSangre : UILabel?
    
    var label = UILabel()
    var lineChart: LineChart!
    
    var paciente : NSDictionary = [ "dict":1 ]

    override func viewDidLoad() {
        super.viewDidLoad()

        label.text = "..."
        label.setTranslatesAutoresizingMaskIntoConstraints(false)
        label.textAlignment = NSTextAlignment.Center
        self.view.addSubview(label)
        
        // Do any additional setup after loading the view.
        lblNombre?.text = paciente.valueForKey("nombre") as? String
        lblFecha?.text = paciente.valueForKey("fechaNacimiento") as? String
        lblSangre?.text = paciente.valueForKey("tipoSangre") as? String
        
        var episodios : NSArray = paciente.valueForKey("episodios") as! NSArray
        
        var vals : NSArray = [episodios.count]
        for (index, element) in enumerate(episodios) {
//            vals[(index as Int)] = (element as NSDictionary).valueForKey("")
            println("Item \(index): \(element)")
        }
        
        lineChart = LineChart()
        lineChart.bounds.origin = CGPointMake(0.0, 0.0)
        lineChart.addLine([3, 4, 9, 11, 13, 15])
        lineChart.area = false
        lineChart.x.grid.count = 5
        lineChart.y.grid.count = 5

        lineChart.setTranslatesAutoresizingMaskIntoConstraints(false)
        lineChart.delegate = self
        self.view.addSubview(lineChart)
    }
    
    func didSelectDataPoint(x: CGFloat, yValues: Array<CGFloat>) {
        label.text = "x: \(x)     y: \(yValues)"
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    func setPacienteVista(npaciente : NSDictionary) -> Void{
        paciente = npaciente
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
