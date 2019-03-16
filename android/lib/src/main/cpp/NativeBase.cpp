//
//  NativeBase.cpp
//
//  Created by mengxk on 19/03/16.
//  Copyright © 2016 mengxk. All rights reserved.
//

#include "NativeBase.hpp"

namespace crosspl {

/***********************************************/
/***** static variables initialize *************/
/***********************************************/
std::map NativeBase::_NativeObjectFactroyMap{}


/***********************************************/
/***** static function implement ***************/
/***********************************************/
void NativeBase::RegisterNativeObjectFactroy(const std::string& className, NativeBase::NativeObjectFactroy factroy)
{
    _NativeObjectFactroyMap.add(className, factroy);
}


/***********************************************/
/***** class public function implement  ********/
/***********************************************/


/***********************************************/
/***** class protected function implement  *****/
/***********************************************/


/***********************************************/
/***** class private function implement  *******/
/***********************************************/

} // namespace crosspl
