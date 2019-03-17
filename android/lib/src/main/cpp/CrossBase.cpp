//
//  CrossBase.cpp
//
//  Created by mengxk on 19/03/16.
//  Copyright © 2016 mengxk. All rights reserved.
//

#include "CrossBase.hpp"

namespace crosspl {

/***********************************************/
/***** static variables initialize *************/
/***********************************************/
std::map CrossBase::_NativeObjectFactroyMap{}


/***********************************************/
/***** static function implement ***************/
/***********************************************/
void CrossBase::RegisterNativeObjectFactroy(const std::string& className, CrossBase::NativeObjectFactroy factroy)
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
