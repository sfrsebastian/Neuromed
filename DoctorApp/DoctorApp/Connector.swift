//
//  Connector.swift
//  DoctorApp
//
//  Created by Felipe Macbook Pro on 3/16/15.
//  Copyright (c) 2015 Felipe-Otalora. All rights reserved.
//

import Foundation
import UIKit

class Connector {
    
    let method : String
    let requestUrl = "https://neuromed.herokuapp.com/api"
    
    init(){
        self.method = ""
    }
    
    func doGet(target : String) -> NSArray{
        var request : NSMutableURLRequest = NSMutableURLRequest()
        request.URL = NSURL(string: requestUrl + target)
        request.HTTPMethod = "GET"
        println(NSUserDefaults.standardUserDefaults().valueForKey("TOKEN"))
        request.addValue(NSUserDefaults.standardUserDefaults().valueForKey("TOKEN") as? String, forHTTPHeaderField: "X-Auth-Token")
        
        var response: NSURLResponse?
        
        var err: AutoreleasingUnsafeMutablePointer<NSError?> = nil
        
        let urlData = NSURLConnection.sendSynchronousRequest(request, returningResponse: &response, error: err)
        
        //WARNING! Check if json response is an ARRAY or a DICTIONARY, in that case, cast the method accordingly
        
        var jsonResult: NSArray = NSJSONSerialization.JSONObjectWithData(urlData!, options:NSJSONReadingOptions.MutableContainers, error:err) as! NSArray
        
        return jsonResult
    }
    
    func doPutData(url : String, data: NSData, params : NSDictionary, filename : String) -> Void{
//        let net = Net()
//        
//        let params = ["grabacion": NetData(data: data, mimeType: MimeType.Json, filename: filename)]
//        //append params to params
//        
//        net.PUT(requestUrl + url, params: params, successHandler: { responseData in
//            //            let result = responseData.json(error: nil)
//            let response : NSData = responseData.data
//            NSLog("result: \(response.description)")
//            }, failureHandler: { error in
//                NSLog("Error while tryinng to call PUT method on \(self.requestUrl + url) sending NSData")
//        })
    }
    
    func doPostFile(url : String, dict : NSDictionary, file : NSData) -> NSArray{
        return NSArray()
    }
    
    func doPost(url : String, dict : NSDictionary) -> NSDictionary{
        var request : NSMutableURLRequest = NSMutableURLRequest()
        request.URL = NSURL(string: requestUrl + url)
        request.HTTPMethod = "POST"
        request.addValue(NSUserDefaults.standardUserDefaults().valueForKey("TOKEN") as? String, forHTTPHeaderField: "X-Auth-Token")
        
        var err : NSError?
        request.HTTPBody = NSJSONSerialization.dataWithJSONObject(dict, options: nil, error: &err)
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        request.addValue("application/json", forHTTPHeaderField: "Accept")
        
        var response: NSURLResponse?
        
        var error: AutoreleasingUnsafeMutablePointer<NSError?> = nil
        
        let urlData = NSURLConnection.sendSynchronousRequest(request, returningResponse: &response, error: error)
        
        //WARNING! Check if json response is an ARRAY or a DICTIONARY, in that case, cast the method accordingly
        var jsonResult : NSDictionary = ["":""]
        
        if(urlData != nil){
            //Handle ERROR!
            jsonResult = NSJSONSerialization.JSONObjectWithData(urlData!, options:NSJSONReadingOptions.MutableContainers, error:error) as! NSDictionary
        }
        
        return jsonResult
    }
    
    func getImage(url : String) -> UIImage{
        var url = NSURL(string: url)
        var data = NSData(contentsOfURL: url!)
        let img : UIImage  = UIImage(data: data!)!
        return img
    }
    
    func doAsyncGet(uri : String) -> NSArray{
        var request : NSMutableURLRequest = NSMutableURLRequest()
        request.URL = NSURL(string: requestUrl)
        request.HTTPMethod = "GET"
        
        NSURLConnection.sendAsynchronousRequest(request, queue: NSOperationQueue(), completionHandler:{ (response:NSURLResponse!, data: NSData!, error: NSError!) -> Void in
            
            var error: AutoreleasingUnsafeMutablePointer<NSError?> = nil
            
            let jsonString: String = String(contentsOfURL: request.URL!, encoding: NSASCIIStringEncoding, error: error)!
            
            let jsonResult: NSDictionary! = NSJSONSerialization.JSONObjectWithData(jsonString.dataUsingEncoding(NSUTF8StringEncoding, allowLossyConversion: false)!, options:NSJSONReadingOptions.MutableContainers, error: error) as? NSDictionary
            
            if (jsonResult != nil) {
                // process jsonResult
                
            } else {
                // couldn't load JSON, look at error
                println("Error: \(error.debugDescription)")
            }
            
            
        })
        return NSArray(object: "null")
    }
}